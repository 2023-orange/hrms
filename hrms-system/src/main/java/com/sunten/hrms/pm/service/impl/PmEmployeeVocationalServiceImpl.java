package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmEmployeeVocationalDao;
import com.sunten.hrms.pm.dao.PmEmployeeVocationalTempDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeVocational;
import com.sunten.hrms.pm.domain.PmEmployeeVocationalTemp;
import com.sunten.hrms.pm.dto.PmEmployeeVocationalDTO;
import com.sunten.hrms.pm.dto.PmEmployeeVocationalQueryCriteria;
import com.sunten.hrms.pm.dto.PmEmployeeVocationalTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeVocationalCheckMapper;
import com.sunten.hrms.pm.mapper.PmEmployeeVocationalMapper;
import com.sunten.hrms.pm.service.PmEmployeeVocationalService;
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
 * 职业资格表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeVocationalServiceImpl extends ServiceImpl<PmEmployeeVocationalDao, PmEmployeeVocational> implements PmEmployeeVocationalService {
    private final PmEmployeeVocationalDao pmEmployeeVocationalDao;
    private final PmEmployeeVocationalMapper pmEmployeeVocationalMapper;
    private final PmEmployeeVocationalCheckMapper pmEmployeeVocationalCheckMapper;
    private final PmEmployeeVocationalTempDao pmEmployeeVocationalTempDao;

    public PmEmployeeVocationalServiceImpl(PmEmployeeVocationalDao pmEmployeeVocationalDao, PmEmployeeVocationalMapper pmEmployeeVocationalMapper,
                                           PmEmployeeVocationalCheckMapper pmEmployeeVocationalCheckMapper, PmEmployeeVocationalTempDao pmEmployeeVocationalTempDao) {
        this.pmEmployeeVocationalDao = pmEmployeeVocationalDao;
        this.pmEmployeeVocationalMapper = pmEmployeeVocationalMapper;
        this.pmEmployeeVocationalCheckMapper = pmEmployeeVocationalCheckMapper;
        this.pmEmployeeVocationalTempDao = pmEmployeeVocationalTempDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeVocationalDTO insert(PmEmployeeVocational employeeVocationalNew) {
        pmEmployeeVocationalDao.insertAllColumn(employeeVocationalNew);
        return pmEmployeeVocationalMapper.toDto(employeeVocationalNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeVocational employeeVocational = new PmEmployeeVocational();
        employeeVocational.setId(id);
        employeeVocational.setEnabledFlag(false);
        this.delete(employeeVocational);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeVocational employeeVocational) {
        //  确认删除前是否需要做检查，不删除，只失效
        pmEmployeeVocationalDao.updateEnableFlag(employeeVocational);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeVocational employeeVocationalNew) {
        PmEmployeeVocational employeeVocationalInDb = Optional.ofNullable(pmEmployeeVocationalDao.getByKey(employeeVocationalNew.getId())).orElseGet(PmEmployeeVocational::new);
        ValidationUtil.isNull(employeeVocationalInDb.getId(), "EmployeeVocational", "id", employeeVocationalNew.getId());
        employeeVocationalNew.setId(employeeVocationalInDb.getId());
        pmEmployeeVocationalDao.updateAllColumnByKey(employeeVocationalNew);
    }

    @Override
    public PmEmployeeVocationalDTO getByKey(Long id) {
        PmEmployeeVocational employeeVocational = Optional.ofNullable(pmEmployeeVocationalDao.getByKey(id)).orElseGet(PmEmployeeVocational::new);
        ValidationUtil.isNull(employeeVocational.getId(), "EmployeeVocational", "id", id);
        return pmEmployeeVocationalMapper.toDto(employeeVocational);
    }

    @Override
    public List<PmEmployeeVocationalDTO> listAll(PmEmployeeVocationalQueryCriteria criteria) {
        return pmEmployeeVocationalMapper.toDto(pmEmployeeVocationalDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeVocationalQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeVocational> page = PageUtil.startPage(pageable);
        List<PmEmployeeVocational> employeeVocationals = pmEmployeeVocationalDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeVocationalMapper.toDto(employeeVocationals), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeVocationalDTO> employeeVocationalDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeVocationalDTO employeeVocationalDTO : employeeVocationalDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", employeeVocationalDTO.getEmployee().getId());
            map.put("资格名称", employeeVocationalDTO.getVocationalName());
            map.put("级别", employeeVocationalDTO.getVocationalLevel());
            map.put("评定时间", employeeVocationalDTO.getEvaluationTime());
            map.put("发证机构", employeeVocationalDTO.getEvaluationMechanism());
            map.put("有效期", employeeVocationalDTO.getVocationalValidity());
            map.put("备注", employeeVocationalDTO.getRemarks());
            map.put("是否当前最高", employeeVocationalDTO.getNewVocationalFlag());
            map.put("弹性域1", employeeVocationalDTO.getAttribute1());
            map.put("弹性域2", employeeVocationalDTO.getAttribute2());
            map.put("弹性域3", employeeVocationalDTO.getAttribute3());
            map.put("有效标记默认值", employeeVocationalDTO.getEnabledFlag());
            map.put("id", employeeVocationalDTO.getId());
            map.put("创建时间", employeeVocationalDTO.getCreateTime());
            map.put("创建人ID", employeeVocationalDTO.getCreateBy());
            map.put("修改时间", employeeVocationalDTO.getUpdateTime());
            map.put("修改人ID", employeeVocationalDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<PmEmployeeVocationalDTO> listAllByCheck(Long employeeId) {
        List<PmEmployeeVocational> pmEmployeeVocationals = pmEmployeeVocationalDao.listAllAndTempByEmployee(employeeId);

        if (pmEmployeeVocationals.size() > 0) {
            for (PmEmployeeVocational pev : pmEmployeeVocationals) {//循环将temp修改记录添加进children
                pev.setIdKey("P" + pev.getId().toString());
                if (pev.getVocationalTemp() != null) {
                    PmEmployeeVocational tempVacational = pmEmployeeVocationalCheckMapper.toEntity(pev.getVocationalTemp());
                    tempVacational.setId(pev.getVocationalTemp().getId());
                    tempVacational.setIdKey("S" + tempVacational.getId().toString());
                    pev.setChildren(new HashSet<>());
                    pev.getChildren().add(tempVacational);
                    pev.setVocationalTemp(null);
                }
            }
        }

        PmEmployeeVocationalTempQueryCriteria criteria = new PmEmployeeVocationalTempQueryCriteria();
        criteria.setEnabled(true);
        criteria.setInstructionsFlag("A");
        criteria.setEmployeeId(employeeId);
        criteria.setCheckFlag("D");
        List<PmEmployeeVocationalTemp> vocationalTemps = pmEmployeeVocationalTempDao.listAllByCriteria(criteria);

        if (vocationalTemps.size() > 0) {
            PmEmployeeVocational addTempVocational = new PmEmployeeVocational();
            addTempVocational.setVocationalName("新增");
            addTempVocational.setChildren(new HashSet<>());
            addTempVocational.setIdKey("NEW");
            for (PmEmployeeVocationalTemp pevt : vocationalTemps) {
                PmEmployeeVocational childrenEducation = pmEmployeeVocationalCheckMapper.toEntity(pevt);
                childrenEducation.setId(pevt.getId());
                childrenEducation.setIdKey("S" + pevt.getId().toString());
                addTempVocational.getChildren().add(childrenEducation);
            }

            pmEmployeeVocationals.add(addTempVocational);
        }

        return pmEmployeeVocationalMapper.toDto(pmEmployeeVocationals);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PmEmployeeVocationalDTO> batchInsert(List<PmEmployeeVocational> employeeVocationals, Long employeeId) {
        for (PmEmployeeVocational pev : employeeVocationals) {
            if (employeeId != null) {
                if (pev.getEmployee() == null) {
                    pev.setEmployee(new PmEmployee());
                }
                pev.getEmployee().setId(employeeId);
            }
            if (pev.getEmployee() == null || pev.getEmployee().getId() == null) {
                throw new InfoCheckWarningMessException("员工id不能为空");
            }
            if (pev.getId() != null) {
                if (pev.getId().equals(-1L)) {
                    pmEmployeeVocationalDao.insertAllColumn(pev);
                } else {
                    pmEmployeeVocationalDao.updateAllColumnByKey(pev);
                }
            } else {
                throw new InfoCheckWarningMessException("职业资格批量插入传入集合中 id 包含空值！");
            }
        }
        return pmEmployeeVocationalMapper.toDto(employeeVocationals);
    }
}
