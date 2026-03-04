package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmEmployeeTeleDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeTele;
import com.sunten.hrms.pm.dto.PmEmployeeTeleDTO;
import com.sunten.hrms.pm.dto.PmEmployeeTeleQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeTeleMapper;
import com.sunten.hrms.pm.service.PmEmployeeTeleService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 办公电话表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeTeleServiceImpl extends ServiceImpl<PmEmployeeTeleDao, PmEmployeeTele> implements PmEmployeeTeleService {
    private final PmEmployeeTeleDao pmEmployeeTeleDao;
    private final PmEmployeeTeleMapper pmEmployeeTeleMapper;

    public PmEmployeeTeleServiceImpl(PmEmployeeTeleDao pmEmployeeTeleDao, PmEmployeeTeleMapper pmEmployeeTeleMapper) {
        this.pmEmployeeTeleDao = pmEmployeeTeleDao;
        this.pmEmployeeTeleMapper = pmEmployeeTeleMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeTeleDTO insert(PmEmployeeTele employeeTeleNew) {
        pmEmployeeTeleDao.insertAllColumn(employeeTeleNew);
        return pmEmployeeTeleMapper.toDto(employeeTeleNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeTele employeeTele = new PmEmployeeTele();
        employeeTele.setId(id);
        employeeTele.setEnabledFlag(false);
        this.delete(employeeTele);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeTele employeeTele) {
        //  确认删除前是否需要做检查,只失效，不删除
        pmEmployeeTeleDao.updateEnableFlag(employeeTele);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeTele employeeTeleNew) {
        PmEmployeeTele employeeTeleInDb = Optional.ofNullable(pmEmployeeTeleDao.getByKey(employeeTeleNew.getId())).orElseGet(PmEmployeeTele::new);
        ValidationUtil.isNull(employeeTeleInDb.getId(), "EmployeeTele", "id", employeeTeleNew.getId());
        employeeTeleNew.setId(employeeTeleInDb.getId());
        pmEmployeeTeleDao.updateAllColumnByKey(employeeTeleNew);
    }

    @Override
    public PmEmployeeTeleDTO getByKey(Long id) {
        PmEmployeeTele employeeTele = Optional.ofNullable(pmEmployeeTeleDao.getByKey(id)).orElseGet(PmEmployeeTele::new);
        ValidationUtil.isNull(employeeTele.getId(), "EmployeeTele", "id", id);
        return pmEmployeeTeleMapper.toDto(employeeTele);
    }

    @Override
    public List<PmEmployeeTeleDTO> listAll(PmEmployeeTeleQueryCriteria criteria) {
        return pmEmployeeTeleMapper.toDto(pmEmployeeTeleDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeTeleQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeTele> page = PageUtil.startPage(pageable);
        List<PmEmployeeTele> employeeTeles = pmEmployeeTeleDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeTeleMapper.toDto(employeeTeles), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeTeleDTO> employeeTeleDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeTeleDTO employeeTeleDTO : employeeTeleDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", employeeTeleDTO.getEmployee().getId());
            map.put("电话类别", employeeTeleDTO.getTeleType());
            map.put("电话号码", employeeTeleDTO.getTele());
            map.put("是否商务", employeeTeleDTO.getTeleBusinessFlag());
            map.put("备注", employeeTeleDTO.getRemarks());
            map.put("是否显示电话", employeeTeleDTO.getTeleEnabledFlag());
            map.put("弹性域1", employeeTeleDTO.getAttribute1());
            map.put("弹性域2", employeeTeleDTO.getAttribute2());
            map.put("弹性域3", employeeTeleDTO.getAttribute3());
            map.put("有效标记默认值", employeeTeleDTO.getEnabledFlag());
            map.put("id", employeeTeleDTO.getId());
            map.put("创建时间", employeeTeleDTO.getCreateTime());
            map.put("创建人ID", employeeTeleDTO.getCreateBy());
            map.put("修改时间", employeeTeleDTO.getUpdateTime());
            map.put("修改人ID", employeeTeleDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<PmEmployeeTeleDTO> batchInsert(List<PmEmployeeTele> teles, Long employeeId) {
        if (teles != null && teles.size() > 0) {
            for (PmEmployeeTele pet : teles) {
                if (employeeId != null) {
                    if (pet.getEmployee() == null) {
                        pet.setEmployee(new PmEmployee());
                    }
                    pet.getEmployee().setId(employeeId);
                }
                if (pet.getEmployee() == null || pet.getEmployee().getId() == null) {
                    throw new InfoCheckWarningMessException("员工id不能为空");
                }
                if (pet.getId() != null) {
                    if (pet.getId().equals(-1L)) {
                        pmEmployeeTeleDao.insertAllColumn(pet);
                    } else {
                        pmEmployeeTeleDao.updateAllColumnByKey(pet);
                    }
                } else {
                    throw new InfoCheckWarningMessException("传入集合中 id 包含空值！");
                }
            }
        }

        return pmEmployeeTeleMapper.toDto(teles);
    }

    @Override
    public void insertRecruitmentTele(PmEmployeeTele tele, Long employeeId) {
        if (employeeId != null) {
            PmEmployeeTele teles = new PmEmployeeTele();
            teles.setEmployeeId(employeeId);
            teles.setTele(tele.getTele());
            pmEmployeeTeleDao.insertByRecruitment(teles);
        }
        else {
            throw new InfoCheckWarningMessException("传入员工电话值的人员ID为空！");
        }
    }
}
