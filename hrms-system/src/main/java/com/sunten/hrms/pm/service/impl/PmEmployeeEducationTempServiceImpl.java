package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.PmEmployeeEducationDao;
import com.sunten.hrms.pm.dao.PmEmployeeEducationTempDao;
import com.sunten.hrms.pm.domain.PmEmployeeEducation;
import com.sunten.hrms.pm.domain.PmEmployeeEducationTemp;
import com.sunten.hrms.pm.dto.PmEmployeeEducationTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeEducationTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeEducationCheckMapper;
import com.sunten.hrms.pm.mapper.PmEmployeeEducationTempMapper;
import com.sunten.hrms.pm.service.PmEmployeeEducationTempService;
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
 * 教育信息临时表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeEducationTempServiceImpl extends ServiceImpl<PmEmployeeEducationTempDao, PmEmployeeEducationTemp> implements PmEmployeeEducationTempService {
    private final PmEmployeeEducationTempDao pmEmployeeEducationTempDao;
    private final PmEmployeeEducationTempMapper pmEmployeeEducationTempMapper;
    private final PmEmployeeEducationDao pmEmployeeEducationDao;
    private final PmEmployeeEducationCheckMapper pmEmployeeEducationCheckMapper;
    private final FndUserService fndUserService;

    public PmEmployeeEducationTempServiceImpl(PmEmployeeEducationTempDao pmEmployeeEducationTempDao, PmEmployeeEducationTempMapper pmEmployeeEducationTempMapper, PmEmployeeEducationDao pmEmployeeEducationDao, PmEmployeeEducationCheckMapper pmEmployeeEducationCheckMapper, FndUserService fndUserService) {
        this.pmEmployeeEducationTempDao = pmEmployeeEducationTempDao;
        this.pmEmployeeEducationTempMapper = pmEmployeeEducationTempMapper;
        this.pmEmployeeEducationDao = pmEmployeeEducationDao;
        this.pmEmployeeEducationCheckMapper = pmEmployeeEducationCheckMapper;
        this.fndUserService = fndUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeEducationTempDTO insert(PmEmployeeEducationTemp employeeEducationTempNew) {
        pmEmployeeEducationTempDao.insertAllColumn(employeeEducationTempNew);
        return pmEmployeeEducationTempMapper.toDto(employeeEducationTempNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeEducationTemp employeeEducationTempInDb = Optional.ofNullable(pmEmployeeEducationTempDao.getByKey(id)).orElseGet(PmEmployeeEducationTemp::new);
        ValidationUtil.isNull(employeeEducationTempInDb.getId(), "EmployeeEducationTemp", "id", employeeEducationTempInDb.getId());
        if (!fndUserService.isActiveUser(employeeEducationTempInDb.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法删除此信息");
        }
        employeeEducationTempInDb.setEnabledFlag(false);
        this.delete(employeeEducationTempInDb);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeEducationTemp employeeEducationTemp) {
        //确认删除前是否需要做检查,只失效，不删除
        pmEmployeeEducationTempDao.updateEnableFlag(employeeEducationTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeEducationTemp employeeEducationTempNew) {
        PmEmployeeEducationTemp employeeEducationTempInDb = Optional.ofNullable(pmEmployeeEducationTempDao.getByKey(employeeEducationTempNew.getId())).orElseGet(PmEmployeeEducationTemp::new);
        ValidationUtil.isNull(employeeEducationTempInDb.getId(), "EmployeeEducationTemp", "id", employeeEducationTempNew.getId());
        employeeEducationTempNew.setId(employeeEducationTempInDb.getId());
        pmEmployeeEducationTempDao.updateAllColumnByKey(employeeEducationTempNew);
    }

    @Override
    public PmEmployeeEducationTempDTO getByKey(Long id) {
        PmEmployeeEducationTemp employeeEducationTemp = Optional.ofNullable(pmEmployeeEducationTempDao.getByKey(id)).orElseGet(PmEmployeeEducationTemp::new);
        ValidationUtil.isNull(employeeEducationTemp.getId(), "EmployeeEducationTemp", "id", id);
        return pmEmployeeEducationTempMapper.toDto(employeeEducationTemp);
    }

    @Override
    public List<PmEmployeeEducationTempDTO> listAll(PmEmployeeEducationTempQueryCriteria criteria) {
        return pmEmployeeEducationTempMapper.toDto(pmEmployeeEducationTempDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeEducationTempQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeEducationTemp> page = PageUtil.startPage(pageable);
        List<PmEmployeeEducationTemp> employeeEducationTemps = pmEmployeeEducationTempDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeEducationTempMapper.toDto(employeeEducationTemps), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeEducationTempDTO> employeeEducationTempDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeEducationTempDTO employeeEducationTempDTO : employeeEducationTempDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("教育id", employeeEducationTempDTO.getEmployeeEducation().getId());
            map.put("毕业学校", employeeEducationTempDTO.getSchool());
            map.put("学历", employeeEducationTempDTO.getEducation());
            map.put("入学时间", employeeEducationTempDTO.getEnrollmentTime());
            map.put("毕业时间", employeeEducationTempDTO.getGraduationTime());
            map.put("是否最高学历", employeeEducationTempDTO.getNewEducationFlag());
            map.put("专业", employeeEducationTempDTO.getSpecializedSubject());
            map.put("入学性质", employeeEducationTempDTO.getEnrollment());
            map.put("操作标记", employeeEducationTempDTO.getInstructionsFlag());
            map.put("校核标记", employeeEducationTempDTO.getCheckFlag());
            map.put("弹性域1", employeeEducationTempDTO.getAttribute1());
            map.put("弹性域2", employeeEducationTempDTO.getAttribute2());
            map.put("弹性域3", employeeEducationTempDTO.getAttribute3());
            map.put("有效标记默认值", employeeEducationTempDTO.getEnabledFlag());
            map.put("id", employeeEducationTempDTO.getId());
            map.put("createBy", employeeEducationTempDTO.getCreateBy());
            map.put("updateTime", employeeEducationTempDTO.getUpdateTime());
            map.put("createTime", employeeEducationTempDTO.getCreateTime());
            map.put("updateBy", employeeEducationTempDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void check(PmEmployeeEducationTemp pmEmployeeEducationTemp) {
        //校核
        pmEmployeeEducationTempDao.updateCheckFlag(pmEmployeeEducationTemp);
        PmEmployeeEducationTemp educationTemp = pmEmployeeEducationTempDao.getByKey(pmEmployeeEducationTemp.getId());
        if ("Y".equals(pmEmployeeEducationTemp.getCheckFlag())) {//
            PmEmployeeEducation pmEmployeeEducation = pmEmployeeEducationCheckMapper.toEntity(educationTemp);
            if ("A".equals(educationTemp.getInstructionsFlag())) {
                pmEmployeeEducationDao.insertAllColumn(pmEmployeeEducation);
            } else if ("U".equals(educationTemp.getInstructionsFlag())) {
                pmEmployeeEducationDao.updateAllColumnByKey(pmEmployeeEducation);
            }
        }
    }
}
