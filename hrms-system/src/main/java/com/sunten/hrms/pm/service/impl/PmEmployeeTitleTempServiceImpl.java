package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.PmEmployeeTitleDao;
import com.sunten.hrms.pm.dao.PmEmployeeTitleTempDao;
import com.sunten.hrms.pm.domain.PmEmployeeTitle;
import com.sunten.hrms.pm.domain.PmEmployeeTitleTemp;
import com.sunten.hrms.pm.dto.PmEmployeeTitleTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeTitleTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeTitleCheckMapper;
import com.sunten.hrms.pm.mapper.PmEmployeeTitleTempMapper;
import com.sunten.hrms.pm.service.PmEmployeeTitleTempService;
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
 * 职称情况临时表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeTitleTempServiceImpl extends ServiceImpl<PmEmployeeTitleTempDao, PmEmployeeTitleTemp> implements PmEmployeeTitleTempService {
    private final PmEmployeeTitleTempDao pmEmployeeTitleTempDao;
    private final PmEmployeeTitleTempMapper pmEmployeeTitleTempMapper;
    private final FndUserService fndUserService;
    private final PmEmployeeTitleDao pmEmployeeTitleDao;
    private final PmEmployeeTitleCheckMapper pmEmployeeTitleCheckMapper;

    public PmEmployeeTitleTempServiceImpl(PmEmployeeTitleTempDao pmEmployeeTitleTempDao, PmEmployeeTitleTempMapper pmEmployeeTitleTempMapper, FndUserService fndUserService, PmEmployeeTitleDao pmEmployeeTitleDao, PmEmployeeTitleCheckMapper pmEmployeeTitleCheckMapper) {
        this.pmEmployeeTitleTempDao = pmEmployeeTitleTempDao;
        this.pmEmployeeTitleTempMapper = pmEmployeeTitleTempMapper;
        this.fndUserService = fndUserService;
        this.pmEmployeeTitleDao = pmEmployeeTitleDao;
        this.pmEmployeeTitleCheckMapper = pmEmployeeTitleCheckMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeTitleTempDTO insert(PmEmployeeTitleTemp employeeTitleTempNew) {
        if (!fndUserService.isActiveUser(employeeTitleTempNew.getEmployeeId())) {
            throw new BadRequestException("不是本人，无法新增");
        }
        pmEmployeeTitleTempDao.insertAllColumn(employeeTitleTempNew);
        return pmEmployeeTitleTempMapper.toDto(employeeTitleTempNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeTitleTemp employeeTitleTempInDb = Optional.ofNullable(pmEmployeeTitleTempDao.getByKey(id)).orElseGet(PmEmployeeTitleTemp::new);
        ValidationUtil.isNull(employeeTitleTempInDb.getId(), "EmployeeTitleTemp", "id", id);
        if (!fndUserService.isActiveUser(employeeTitleTempInDb.getEmployeeId())) {
            throw new BadRequestException("不是本人，无法删除");
        }
        employeeTitleTempInDb.setEnabledFlag(false);
        this.delete(employeeTitleTempInDb);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeTitleTemp employeeTitleTemp) {
        // TODO    确认删除前是否需要做检查
        pmEmployeeTitleTempDao.updateEnableFlag(employeeTitleTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeTitleTemp employeeTitleTempNew) {
        PmEmployeeTitleTemp employeeTitleTempInDb = Optional.ofNullable(pmEmployeeTitleTempDao.getByKey(employeeTitleTempNew.getId())).orElseGet(PmEmployeeTitleTemp::new);
        ValidationUtil.isNull(employeeTitleTempInDb.getId(), "EmployeeTitleTemp", "id", employeeTitleTempNew.getId());
        if (!fndUserService.isActiveUser(employeeTitleTempNew.getEmployeeId())) {
            throw new BadRequestException("不是本人，无法修改");
        }
        employeeTitleTempNew.setId(employeeTitleTempInDb.getId());
        pmEmployeeTitleTempDao.updateAllColumnByKey(employeeTitleTempNew);
    }

    @Override
    public PmEmployeeTitleTempDTO getByKey(Long id) {
        PmEmployeeTitleTemp employeeTitleTemp = Optional.ofNullable(pmEmployeeTitleTempDao.getByKey(id)).orElseGet(PmEmployeeTitleTemp::new);
        ValidationUtil.isNull(employeeTitleTemp.getId(), "EmployeeTitleTemp", "id", id);
        return pmEmployeeTitleTempMapper.toDto(employeeTitleTemp);
    }

    @Override
    public List<PmEmployeeTitleTempDTO> listAll(PmEmployeeTitleTempQueryCriteria criteria) {
        return pmEmployeeTitleTempMapper.toDto(pmEmployeeTitleTempDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeTitleTempQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeTitleTemp> page = PageUtil.startPage(pageable);
        List<PmEmployeeTitleTemp> employeeTitleTemps = pmEmployeeTitleTempDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeTitleTempMapper.toDto(employeeTitleTemps), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeTitleTempDTO> employeeTitleTempDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeTitleTempDTO employeeTitleTempDTO : employeeTitleTempDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("职称id", employeeTitleTempDTO.getEmployeeTitle().getId());
            map.put("职称级别", employeeTitleTempDTO.getTitleLevel());
            map.put("职称名称", employeeTitleTempDTO.getTitleName());
            map.put("评定时间", employeeTitleTempDTO.getEvaluationTime());
            map.put("是否最高职称", employeeTitleTempDTO.getNewTitleFlag());
            map.put("操作标记", employeeTitleTempDTO.getInstructionsFlag());
            map.put("校核标记", employeeTitleTempDTO.getCheckFlag());
            map.put("弹性域1", employeeTitleTempDTO.getAttribute1());
            map.put("弹性域2", employeeTitleTempDTO.getAttribute2());
            map.put("弹性域3", employeeTitleTempDTO.getAttribute3());
            map.put("有效标记默认值", employeeTitleTempDTO.getEnabledFlag());
            map.put("id", employeeTitleTempDTO.getId());
            map.put("updateTime", employeeTitleTempDTO.getUpdateTime());
            map.put("createBy", employeeTitleTempDTO.getCreateBy());
            map.put("createTime", employeeTitleTempDTO.getCreateTime());
            map.put("updateBy", employeeTitleTempDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void check(PmEmployeeTitleTemp employeeTitleTemp) {
        //校核
        pmEmployeeTitleTempDao.updateCheckFlag(employeeTitleTemp);
        PmEmployeeTitleTemp titleTemp = pmEmployeeTitleTempDao.getByKey(employeeTitleTemp.getId());
        if ("Y".equals(titleTemp.getCheckFlag())) {
            PmEmployeeTitle employeeTitle = pmEmployeeTitleCheckMapper.toEntity(titleTemp);
            if ("A".equals(titleTemp.getInstructionsFlag())) {
                pmEmployeeTitleDao.insertAllColumn(employeeTitle);
            } else if ("U".equals(titleTemp.getInstructionsFlag())) {
                pmEmployeeTitleDao.updateAllColumnByKey(employeeTitle);
            }
        }
    }
}
