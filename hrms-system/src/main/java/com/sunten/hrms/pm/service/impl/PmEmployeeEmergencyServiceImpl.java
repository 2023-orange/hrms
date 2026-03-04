package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmEmployeeEmergencyDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeEmergency;
import com.sunten.hrms.pm.dto.PmEmployeeEmergencyDTO;
import com.sunten.hrms.pm.dto.PmEmployeeEmergencyQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeEmergencyMapper;
import com.sunten.hrms.pm.service.PmEmployeeEmergencyService;
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
 * 紧急电话表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeEmergencyServiceImpl extends ServiceImpl<PmEmployeeEmergencyDao, PmEmployeeEmergency> implements PmEmployeeEmergencyService {
    private final PmEmployeeEmergencyDao pmEmployeeEmergencyDao;
    private final PmEmployeeEmergencyMapper pmEmployeeEmergencyMapper;

    public PmEmployeeEmergencyServiceImpl(PmEmployeeEmergencyDao pmEmployeeEmergencyDao, PmEmployeeEmergencyMapper pmEmployeeEmergencyMapper) {
        this.pmEmployeeEmergencyDao = pmEmployeeEmergencyDao;
        this.pmEmployeeEmergencyMapper = pmEmployeeEmergencyMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeEmergencyDTO insert(PmEmployeeEmergency employeeEmergencyNew) {
        pmEmployeeEmergencyDao.insertAllColumn(employeeEmergencyNew);
        return pmEmployeeEmergencyMapper.toDto(employeeEmergencyNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeEmergency employeeEmergency = new PmEmployeeEmergency();
        employeeEmergency.setId(id);
        employeeEmergency.setEnabledFlag(false);
        this.delete(employeeEmergency);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeEmergency employeeEmergency) {
        // 确认删除前是否需要做检查,目前只失效，不删除
        pmEmployeeEmergencyDao.updateEnableFlagByKey(employeeEmergency);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeEmergency employeeEmergencyNew) {
        PmEmployeeEmergency employeeEmergencyInDb = Optional.ofNullable(pmEmployeeEmergencyDao.getByKey(employeeEmergencyNew.getId())).orElseGet(PmEmployeeEmergency::new);
        ValidationUtil.isNull(employeeEmergencyInDb.getId(), "EmployeeEmergency", "id", employeeEmergencyNew.getId());
        employeeEmergencyNew.setId(employeeEmergencyInDb.getId());
        pmEmployeeEmergencyDao.updateAllColumnByKey(employeeEmergencyNew);
    }

    @Override
    public PmEmployeeEmergencyDTO getByKey(Long id) {
        PmEmployeeEmergency employeeEmergency = Optional.ofNullable(pmEmployeeEmergencyDao.getByKey(id)).orElseGet(PmEmployeeEmergency::new);
        ValidationUtil.isNull(employeeEmergency.getId(), "EmployeeEmergency", "id", id);
        return pmEmployeeEmergencyMapper.toDto(employeeEmergency);
    }

    @Override
    public List<PmEmployeeEmergencyDTO> listAll(PmEmployeeEmergencyQueryCriteria criteria) {
        return pmEmployeeEmergencyMapper.toDto(pmEmployeeEmergencyDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeEmergencyQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeEmergency> page = PageUtil.startPage(pageable);
        List<PmEmployeeEmergency> employeeEmergencys = pmEmployeeEmergencyDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeEmergencyMapper.toDto(employeeEmergencys), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeEmergencyDTO> employeeEmergencyDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeEmergencyDTO employeeEmergencyDTO : employeeEmergencyDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", employeeEmergencyDTO.getEmployee().getId());
            map.put("紧急联系人", employeeEmergencyDTO.getEmergencyContact());
            map.put("紧急电话", employeeEmergencyDTO.getEmergencyTele());
            map.put("备注", employeeEmergencyDTO.getRemarks());
            map.put("弹性域1", employeeEmergencyDTO.getAttribute1());
            map.put("弹性域2", employeeEmergencyDTO.getAttribute2());
            map.put("弹性域3", employeeEmergencyDTO.getAttribute3());
            map.put("有效标记默认值", employeeEmergencyDTO.getEnabledFlag());
            map.put("id", employeeEmergencyDTO.getId());
            map.put("创建时间", employeeEmergencyDTO.getCreateTime());
            map.put("创建人ID", employeeEmergencyDTO.getCreateBy());
            map.put("修改时间", employeeEmergencyDTO.getUpdateTime());
            map.put("修改人ID", employeeEmergencyDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<PmEmployeeEmergencyDTO> batchInsert(List<PmEmployeeEmergency> employeeEmergencys, Long employeeId) {
        if (employeeEmergencys != null) {
            for (PmEmployeeEmergency pee : employeeEmergencys) {
                if (employeeId != null) {
                    if (pee.getEmployee() == null) {
                        pee.setEmployee(new PmEmployee());
                    }
                    pee.getEmployee().setId(employeeId);
                }
                if (pee.getEmployee() == null || pee.getEmployee().getId() == null) {
                    throw new InfoCheckWarningMessException("员工id不能为空");
                }
                if (pee.getId() != null) {
                    if (pee.getId().equals(-1L)) {
                        pmEmployeeEmergencyDao.insertAllColumn(pee);
                    } else {
                        pmEmployeeEmergencyDao.updateAllColumnByKey(pee);
                    }
                } else {
                    throw new InfoCheckWarningMessException("传入集合中 id 包含空值！");
                }
            }
        }
        return pmEmployeeEmergencyMapper.toDto(employeeEmergencys);
    }

    @Override
    public void insertRecruitmentEmergency(PmEmployeeEmergency employeeEmergency, Long employeeId) {
        if (employeeId != null) {
            PmEmployeeEmergency emergencys = new PmEmployeeEmergency();
            emergencys.setEmployeeId(employeeId);
            emergencys.setEmergencyContact(employeeEmergency.getEmergencyContact());
            emergencys.setEmergencyTele(employeeEmergency.getEmergencyTele());
            pmEmployeeEmergencyDao.insertByRecruitment(emergencys);
        }
        else {
            throw new InfoCheckWarningMessException("传入员工紧急联系人的人员ID为空！");
        }
    }
}
