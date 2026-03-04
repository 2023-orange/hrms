package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmEmployeeEducationDao;
import com.sunten.hrms.pm.dao.PmEmployeeEducationTempDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeEducation;
import com.sunten.hrms.pm.domain.PmEmployeeEducationTemp;
import com.sunten.hrms.pm.dto.PmEmployeeEducationDTO;
import com.sunten.hrms.pm.dto.PmEmployeeEducationQueryCriteria;
import com.sunten.hrms.pm.dto.PmEmployeeEducationTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeEducationCheckMapper;
import com.sunten.hrms.pm.mapper.PmEmployeeEducationMapper;
import com.sunten.hrms.pm.service.PmEmployeeEducationService;
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
 * 教育信息表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeEducationServiceImpl extends ServiceImpl<PmEmployeeEducationDao, PmEmployeeEducation> implements PmEmployeeEducationService {
    private final PmEmployeeEducationDao pmEmployeeEducationDao;
    private final PmEmployeeEducationMapper pmEmployeeEducationMapper;
    private final PmEmployeeEducationTempDao pmEmployeeEducationTempDao;
    private final PmEmployeeEducationCheckMapper pmEmployeeEducationCheckMapper;

    public PmEmployeeEducationServiceImpl(PmEmployeeEducationDao pmEmployeeEducationDao, PmEmployeeEducationMapper pmEmployeeEducationMapper,
                                          PmEmployeeEducationTempDao pmEmployeeEducationTempDao, PmEmployeeEducationCheckMapper pmEmployeeEducationCheckMapper) {
        this.pmEmployeeEducationDao = pmEmployeeEducationDao;
        this.pmEmployeeEducationMapper = pmEmployeeEducationMapper;
        this.pmEmployeeEducationTempDao = pmEmployeeEducationTempDao;
        this.pmEmployeeEducationCheckMapper = pmEmployeeEducationCheckMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeEducationDTO insert(PmEmployeeEducation employeeEducationNew) {
        if (employeeEducationNew.getNewEducationFlag()) {
            PmEmployeeEducation pmEmployeeEducation = pmEmployeeEducationDao.getMainEducationByKey(employeeEducationNew.getEmployee().getId());
            if (pmEmployeeEducation != null) {
                pmEmployeeEducationDao.updateNewEducationFlag(pmEmployeeEducation.setNewEducationFlag(false));
            }
        }
        pmEmployeeEducationDao.insertAllColumn(employeeEducationNew);
        return pmEmployeeEducationMapper.toDto(employeeEducationNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeEducation employeeEducation = new PmEmployeeEducation();
        employeeEducation.setId(id);
        employeeEducation.setEnabledFlag(false);
        this.delete(employeeEducation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeEducation employeeEducation) {
        // 确认删除前是否需要做检查,只失效，不删除
        pmEmployeeEducationDao.updateEnableFlag(employeeEducation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeEducation employeeEducationNew) {
        if (employeeEducationNew.getNewEducationFlag()) {
            PmEmployeeEducation pmEmployeeEducation = pmEmployeeEducationDao.getMainEducationByKey(employeeEducationNew.getEmployee().getId());
            if (pmEmployeeEducation != null) {
                pmEmployeeEducationDao.updateNewEducationFlag(pmEmployeeEducation.setNewEducationFlag(false));
            }
        }
        PmEmployeeEducation employeeEducationInDb = Optional.ofNullable(pmEmployeeEducationDao.getByKey(employeeEducationNew.getId())).orElseGet(PmEmployeeEducation::new);
        ValidationUtil.isNull(employeeEducationInDb.getId(), "EmployeeEducation", "id", employeeEducationNew.getId());
        employeeEducationNew.setId(employeeEducationInDb.getId());
        pmEmployeeEducationDao.updateAllColumnByKey(employeeEducationNew);
    }

    @Override
    public PmEmployeeEducationDTO getByKey(Long id) {
        PmEmployeeEducation employeeEducation = Optional.ofNullable(pmEmployeeEducationDao.getByKey(id)).orElseGet(PmEmployeeEducation::new);
        ValidationUtil.isNull(employeeEducation.getId(), "EmployeeEducation", "id", id);
        return pmEmployeeEducationMapper.toDto(employeeEducation);
    }

    @Override
    public List<PmEmployeeEducationDTO> listAll(PmEmployeeEducationQueryCriteria criteria) {
        return pmEmployeeEducationMapper.toDto(pmEmployeeEducationDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeEducationQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeEducation> page = PageUtil.startPage(pageable);
        List<PmEmployeeEducation> employeeEducations = pmEmployeeEducationDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeEducationMapper.toDto(employeeEducations), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeEducationDTO> employeeEducationDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeEducationDTO employeeEducationDTO : employeeEducationDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", employeeEducationDTO.getEmployee().getId());
            map.put("毕业学校", employeeEducationDTO.getSchool());
            map.put("学历", employeeEducationDTO.getEducation());
            map.put("入学时间", employeeEducationDTO.getEnrollmentTime());
            map.put("毕业时间", employeeEducationDTO.getGraduationTime());
            map.put("是否最高学历", employeeEducationDTO.getNewEducationFlag());
            map.put("专业", employeeEducationDTO.getSpecializedSubject());
            map.put("入学性质", employeeEducationDTO.getEnrollment());
            map.put("弹性域1", employeeEducationDTO.getAttribute1());
            map.put("弹性域2", employeeEducationDTO.getAttribute2());
            map.put("弹性域3", employeeEducationDTO.getAttribute3());
            map.put("有效标记默认值", employeeEducationDTO.getEnabledFlag());
            map.put("id", employeeEducationDTO.getId());
            map.put("创建时间", employeeEducationDTO.getCreateTime());
            map.put("创建人ID", employeeEducationDTO.getCreateBy());
            map.put("修改时间", employeeEducationDTO.getUpdateTime());
            map.put("修改人ID", employeeEducationDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PmEmployeeEducationDTO> batchInsert(List<PmEmployeeEducation> employeeEducations, Long employeeId) {
        if (employeeEducations != null) {
            for (PmEmployeeEducation pee : employeeEducations) {
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
                        pmEmployeeEducationDao.insertAllColumn(pee);
                    } else {
                        pmEmployeeEducationDao.updateAllColumnByKey(pee);
                    }
                } else {
                    throw new InfoCheckWarningMessException("学历批量插入传入集合中 id 包含空值！");
                }
            }
        }

        return pmEmployeeEducationMapper.toDto(employeeEducations);
    }

    @Override
    public List<PmEmployeeEducationDTO> listAllByCheck(Long employeeId) {
        List<PmEmployeeEducation> pmEmployeeEducations = pmEmployeeEducationDao.listAllAndTempByEmployee(employeeId);//正式学历及其temp记录sssss

        if (pmEmployeeEducations.size() > 0) {
            for (PmEmployeeEducation pee : pmEmployeeEducations) {//循环将temp修改记录添加进children
                pee.setIdKey("P" + pee.getId().toString());
                if (pee.getEducationTemp() != null) {
                    PmEmployeeEducation tempEducation = pmEmployeeEducationCheckMapper.toEntity(pee.getEducationTemp());
                    tempEducation.setId(pee.getEducationTemp().getId());
                    tempEducation.setIdKey("S" + tempEducation.getId().toString());
                    pee.setChildren(new HashSet<>());
                    pee.getChildren().add(tempEducation);
                    pee.setEducationTemp(null);
                }
            }
        }
        /*查询新添加的待校核数据*/
        PmEmployeeEducationTempQueryCriteria educationTempQueryCriteria = new PmEmployeeEducationTempQueryCriteria();
        educationTempQueryCriteria.setEnabled(true);//生效
        educationTempQueryCriteria.setEmployeeId(employeeId);//员工
        educationTempQueryCriteria.setInstructionsFlag("A");
        educationTempQueryCriteria.setCheckFlag("D");
        List<PmEmployeeEducationTemp> pmEmployeeEducationTemps = pmEmployeeEducationTempDao.listAllByCriteria(educationTempQueryCriteria);
        if (pmEmployeeEducationTemps.size() > 0) {
            PmEmployeeEducation addTempEducation = new PmEmployeeEducation();
            addTempEducation.setEducation("新增");
            addTempEducation.setChildren(new HashSet<>());
            addTempEducation.setIdKey("NEW");
            for (PmEmployeeEducationTemp peet : pmEmployeeEducationTemps) {
                PmEmployeeEducation childrenEducation = pmEmployeeEducationCheckMapper.toEntity(peet);
                childrenEducation.setId(peet.getId());
                childrenEducation.setIdKey("S" + peet.getId().toString());
                addTempEducation.getChildren().add(childrenEducation);
            }
            pmEmployeeEducations.add(addTempEducation);
        }

        return pmEmployeeEducationMapper.toDto(pmEmployeeEducations);
    }
}
