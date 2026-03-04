package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmEmployeeSocialrelationsDao;
import com.sunten.hrms.pm.dao.PmEmployeeSocialrelationsTempDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeSocialrelations;
import com.sunten.hrms.pm.domain.PmEmployeeSocialrelationsTemp;
import com.sunten.hrms.pm.dto.PmEmployeeSocialrelationsDTO;
import com.sunten.hrms.pm.dto.PmEmployeeSocialrelationsQueryCriteria;
import com.sunten.hrms.pm.dto.PmEmployeeSocialrelationsTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeSocialrelationsCheckMapper;
import com.sunten.hrms.pm.mapper.PmEmployeeSocialrelationsMapper;
import com.sunten.hrms.pm.service.PmEmployeeSocialrelationsService;
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
 * 社会关系表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeSocialrelationsServiceImpl extends ServiceImpl<PmEmployeeSocialrelationsDao, PmEmployeeSocialrelations> implements PmEmployeeSocialrelationsService {
    private final PmEmployeeSocialrelationsDao pmEmployeeSocialrelationsDao;
    private final PmEmployeeSocialrelationsMapper pmEmployeeSocialrelationsMapper;
    private final PmEmployeeSocialrelationsCheckMapper pmEmployeeSocialrelationsCheckMapper;
    private final PmEmployeeSocialrelationsTempDao pmEmployeeSocialrelationsTempDao;

    public PmEmployeeSocialrelationsServiceImpl(PmEmployeeSocialrelationsDao pmEmployeeSocialrelationsDao, PmEmployeeSocialrelationsMapper pmEmployeeSocialrelationsMapper, PmEmployeeSocialrelationsCheckMapper pmEmployeeSocialrelationsCheckMapper, PmEmployeeSocialrelationsTempDao pmEmployeeSocialrelationsTempDao) {
        this.pmEmployeeSocialrelationsDao = pmEmployeeSocialrelationsDao;
        this.pmEmployeeSocialrelationsMapper = pmEmployeeSocialrelationsMapper;
        this.pmEmployeeSocialrelationsCheckMapper = pmEmployeeSocialrelationsCheckMapper;
        this.pmEmployeeSocialrelationsTempDao = pmEmployeeSocialrelationsTempDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeSocialrelationsDTO insert(PmEmployeeSocialrelations employeeSocialrelationsNew) {
        pmEmployeeSocialrelationsDao.insertAllColumn(employeeSocialrelationsNew);
        return pmEmployeeSocialrelationsMapper.toDto(employeeSocialrelationsNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeSocialrelations employeeSocialrelations = new PmEmployeeSocialrelations();
        employeeSocialrelations.setId(id);
        employeeSocialrelations.setEnabledFlag(false);
        this.delete(employeeSocialrelations);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeSocialrelations employeeSocialrelations) {
        // 确认删除前是否需要做检查,只失效，不删除
        pmEmployeeSocialrelationsDao.updateEnableFlag(employeeSocialrelations);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeSocialrelations employeeSocialrelationsNew) {
        PmEmployeeSocialrelations employeeSocialrelationsInDb = Optional.ofNullable(pmEmployeeSocialrelationsDao.getByKey(employeeSocialrelationsNew.getId())).orElseGet(PmEmployeeSocialrelations::new);
        ValidationUtil.isNull(employeeSocialrelationsInDb.getId(), "EmployeeSocialrelations", "id", employeeSocialrelationsNew.getId());
        employeeSocialrelationsNew.setId(employeeSocialrelationsInDb.getId());
        pmEmployeeSocialrelationsDao.updateAllColumnByKey(employeeSocialrelationsNew);
    }

    @Override
    public PmEmployeeSocialrelationsDTO getByKey(Long id) {
        PmEmployeeSocialrelations employeeSocialrelations = Optional.ofNullable(pmEmployeeSocialrelationsDao.getByKey(id)).orElseGet(PmEmployeeSocialrelations::new);
        ValidationUtil.isNull(employeeSocialrelations.getId(), "EmployeeSocialrelations", "id", id);
        return pmEmployeeSocialrelationsMapper.toDto(employeeSocialrelations);
    }

    @Override
    public List<PmEmployeeSocialrelationsDTO> listAll(PmEmployeeSocialrelationsQueryCriteria criteria) {
        return pmEmployeeSocialrelationsMapper.toDto(pmEmployeeSocialrelationsDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeSocialrelationsQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeSocialrelations> page = PageUtil.startPage(pageable);
        List<PmEmployeeSocialrelations> employeeSocialrelationss = pmEmployeeSocialrelationsDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeSocialrelationsMapper.toDto(employeeSocialrelationss), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeSocialrelationsDTO> employeeSocialrelationsDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeSocialrelationsDTO employeeSocialrelationsDTO : employeeSocialrelationsDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", employeeSocialrelationsDTO.getEmployee().getId());
            map.put("姓名", employeeSocialrelationsDTO.getName());
            map.put("关系", employeeSocialrelationsDTO.getRelationship());
            map.put("单位", employeeSocialrelationsDTO.getCompany());
            map.put("职务", employeeSocialrelationsDTO.getPost());
            map.put("电话", employeeSocialrelationsDTO.getTele());
            map.put("是否在厂工作", employeeSocialrelationsDTO.getInFactoryFlag());
            map.put("弹性域1", employeeSocialrelationsDTO.getAttribute1());
            map.put("弹性域2", employeeSocialrelationsDTO.getAttribute2());
            map.put("弹性域3", employeeSocialrelationsDTO.getAttribute3());
            map.put("有效标记默认值", employeeSocialrelationsDTO.getEnabledFlag());
            map.put("id", employeeSocialrelationsDTO.getId());
            map.put("创建时间", employeeSocialrelationsDTO.getCreateTime());
            map.put("创建人ID", employeeSocialrelationsDTO.getCreateBy());
            map.put("修改时间", employeeSocialrelationsDTO.getUpdateTime());
            map.put("修改人ID", employeeSocialrelationsDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<PmEmployeeSocialrelationsDTO> batchInsert(List<PmEmployeeSocialrelations> pmEmployeeSocialrelations, Long employeeId) {
        for (PmEmployeeSocialrelations pes : pmEmployeeSocialrelations) {
            if (employeeId != null) {
                if (pes.getEmployee() == null) {
                    pes.setEmployee(new PmEmployee());
                }
                pes.getEmployee().setId(employeeId);
            }
            if (pes.getEmployee() == null || pes.getEmployee().getId() == null) {
                throw new InfoCheckWarningMessException("员工id不能为空");
            }
            if (pes.getId() != null) {
                if (pes.getId().equals(-1L)) {
                    pmEmployeeSocialrelationsDao.insertAllColumn(pes);
                } else {
                    pmEmployeeSocialrelationsDao.updateAllColumnByKey(pes);
                }
            } else {
                throw new InfoCheckWarningMessException("社会关系批量插入时ID不能为空");
            }

        }

        return pmEmployeeSocialrelationsMapper.toDto(pmEmployeeSocialrelations);
    }

    @Override
    public List<PmEmployeeSocialrelationsDTO> listAllByCheck(Long employeeId) {
        List<PmEmployeeSocialrelations> pmEmployeeSocialrelationss = pmEmployeeSocialrelationsDao.listAllAndTempByEmployee(employeeId);//正式学历及其temp记录sssss

        if (pmEmployeeSocialrelationss.size() > 0) {
            for (PmEmployeeSocialrelations pee : pmEmployeeSocialrelationss) {//循环将temp修改记录添加进children
                pee.setIdKey("P" + pee.getId().toString());
                if (pee.getSocialrelationsTemp() != null) {
                    PmEmployeeSocialrelations tempSocialrelations = pmEmployeeSocialrelationsCheckMapper.toEntity(pee.getSocialrelationsTemp());
                    tempSocialrelations.setId(pee.getSocialrelationsTemp().getId());
                    tempSocialrelations.setIdKey("S" + tempSocialrelations.getId().toString());
                    pee.setChildren(new HashSet<>());
                    pee.getChildren().add(tempSocialrelations);
                    pee.setSocialrelationsTemp(null);
                }
            }
        }
        /*查询新添加的待校核数据*/
        PmEmployeeSocialrelationsTempQueryCriteria SocialrelationsTempQueryCriteria = new PmEmployeeSocialrelationsTempQueryCriteria();
        SocialrelationsTempQueryCriteria.setEnabled(true);//生效
        SocialrelationsTempQueryCriteria.setEmployeeId(employeeId);//员工
        SocialrelationsTempQueryCriteria.setInstructionsFlag("A");
        SocialrelationsTempQueryCriteria.setCheckFlag("D");
        List<PmEmployeeSocialrelationsTemp> pmEmployeeSocialrelationsTemps = pmEmployeeSocialrelationsTempDao.listAllByCriteria(SocialrelationsTempQueryCriteria);
        if (pmEmployeeSocialrelationsTemps.size() > 0) {
            PmEmployeeSocialrelations addTempSocialrelations = new PmEmployeeSocialrelations();
            addTempSocialrelations.setRelationship("新增");
            addTempSocialrelations.setChildren(new HashSet<>());
            addTempSocialrelations.setIdKey("NEW");
            for (PmEmployeeSocialrelationsTemp peet : pmEmployeeSocialrelationsTemps) {
                PmEmployeeSocialrelations childrenSocialrelations = pmEmployeeSocialrelationsCheckMapper.toEntity(peet);
                childrenSocialrelations.setId(peet.getId());
                childrenSocialrelations.setIdKey("S" + peet.getId().toString());
                addTempSocialrelations.getChildren().add(childrenSocialrelations);
            }
            pmEmployeeSocialrelationss.add(addTempSocialrelations);
        }

        return pmEmployeeSocialrelationsMapper.toDto(pmEmployeeSocialrelationss);
    }
}
