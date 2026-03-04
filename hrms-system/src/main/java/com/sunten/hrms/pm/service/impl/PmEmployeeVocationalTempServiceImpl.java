package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.PmEmployeeVocationalDao;
import com.sunten.hrms.pm.dao.PmEmployeeVocationalTempDao;
import com.sunten.hrms.pm.domain.PmEmployeeVocational;
import com.sunten.hrms.pm.domain.PmEmployeeVocationalTemp;
import com.sunten.hrms.pm.dto.PmEmployeeVocationalTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeVocationalTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeVocationalCheckMapper;
import com.sunten.hrms.pm.mapper.PmEmployeeVocationalTempMapper;
import com.sunten.hrms.pm.service.PmEmployeeVocationalTempService;
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
 * 职业资格临时表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeVocationalTempServiceImpl extends ServiceImpl<PmEmployeeVocationalTempDao, PmEmployeeVocationalTemp> implements PmEmployeeVocationalTempService {
    private final PmEmployeeVocationalTempDao pmEmployeeVocationalTempDao;
    private final PmEmployeeVocationalTempMapper pmEmployeeVocationalTempMapper;
    private final PmEmployeeVocationalDao pmEmployeeVocationalDao;
    private final PmEmployeeVocationalCheckMapper pmEmployeeVocationalCheckMapper;
    private final FndUserService fndUserService;

    public PmEmployeeVocationalTempServiceImpl(PmEmployeeVocationalTempDao pmEmployeeVocationalTempDao, PmEmployeeVocationalTempMapper pmEmployeeVocationalTempMapper, PmEmployeeVocationalDao pmEmployeeVocationalDao, PmEmployeeVocationalCheckMapper pmEmployeeVocationalCheckMapper, FndUserService fndUserService) {
        this.pmEmployeeVocationalTempDao = pmEmployeeVocationalTempDao;
        this.pmEmployeeVocationalTempMapper = pmEmployeeVocationalTempMapper;
        this.pmEmployeeVocationalDao = pmEmployeeVocationalDao;
        this.pmEmployeeVocationalCheckMapper = pmEmployeeVocationalCheckMapper;
        this.fndUserService = fndUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeVocationalTempDTO insert(PmEmployeeVocationalTemp employeeVocationalTempNew) {
        pmEmployeeVocationalTempDao.insertAllColumn(employeeVocationalTempNew);
        return pmEmployeeVocationalTempMapper.toDto(employeeVocationalTempNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeVocationalTemp employeeVocationalTempInDb = Optional.ofNullable(pmEmployeeVocationalTempDao.getByKey(id)).orElseGet(PmEmployeeVocationalTemp::new);
        ValidationUtil.isNull(employeeVocationalTempInDb.getId(), "EmployeeVocationalTemp", "id", employeeVocationalTempInDb.getId());
        if (!fndUserService.isActiveUser(employeeVocationalTempInDb.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法删除");
        }
        employeeVocationalTempInDb.setEnabledFlag(false);
        this.delete(employeeVocationalTempInDb);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeVocationalTemp employeeVocationalTemp) {
        // 确认删除前是否需要做检查，只失效，不删除

        pmEmployeeVocationalTempDao.updateEnableFalg(employeeVocationalTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeVocationalTemp employeeVocationalTempNew) {
        PmEmployeeVocationalTemp employeeVocationalTempInDb = Optional.ofNullable(pmEmployeeVocationalTempDao.getByKey(employeeVocationalTempNew.getId())).orElseGet(PmEmployeeVocationalTemp::new);
        ValidationUtil.isNull(employeeVocationalTempInDb.getId(), "EmployeeVocationalTemp", "id", employeeVocationalTempNew.getId());
        employeeVocationalTempNew.setId(employeeVocationalTempInDb.getId());
        pmEmployeeVocationalTempDao.updateAllColumnByKey(employeeVocationalTempNew);
    }

    @Override
    public PmEmployeeVocationalTempDTO getByKey(Long id) {
        PmEmployeeVocationalTemp employeeVocationalTemp = Optional.ofNullable(pmEmployeeVocationalTempDao.getByKey(id)).orElseGet(PmEmployeeVocationalTemp::new);
        ValidationUtil.isNull(employeeVocationalTemp.getId(), "EmployeeVocationalTemp", "id", id);
        return pmEmployeeVocationalTempMapper.toDto(employeeVocationalTemp);
    }

    @Override
    public List<PmEmployeeVocationalTempDTO> listAll(PmEmployeeVocationalTempQueryCriteria criteria) {
        return pmEmployeeVocationalTempMapper.toDto(pmEmployeeVocationalTempDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeVocationalTempQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeVocationalTemp> page = PageUtil.startPage(pageable);
        List<PmEmployeeVocationalTemp> employeeVocationalTemps = pmEmployeeVocationalTempDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeVocationalTempMapper.toDto(employeeVocationalTemps), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeVocationalTempDTO> employeeVocationalTempDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeVocationalTempDTO employeeVocationalTempDTO : employeeVocationalTempDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("资格id", employeeVocationalTempDTO.getEmployeeVocational().getId());
            map.put("资格名称", employeeVocationalTempDTO.getVocationalName());
            map.put("级别", employeeVocationalTempDTO.getVocationalLevel());
            map.put("评定时间", employeeVocationalTempDTO.getEvaluationTime());
            map.put("发证机构", employeeVocationalTempDTO.getEvaluationMechanism());
            map.put("有效期", employeeVocationalTempDTO.getVocationalValidity());
            map.put("备注", employeeVocationalTempDTO.getRemarks());
            map.put("是否当前最高", employeeVocationalTempDTO.getNewVocationalFlag());
            map.put("操作标记", employeeVocationalTempDTO.getInstructionsFlag());
            map.put("校核标记", employeeVocationalTempDTO.getCheckFlag());
            map.put("弹性域1", employeeVocationalTempDTO.getAttribute1());
            map.put("弹性域2", employeeVocationalTempDTO.getAttribute2());
            map.put("弹性域3", employeeVocationalTempDTO.getAttribute3());
            map.put("id", employeeVocationalTempDTO.getId());
            map.put("enabledFlag", employeeVocationalTempDTO.getEnabledFlag());
            map.put("updateBy", employeeVocationalTempDTO.getUpdateBy());
            map.put("createTime", employeeVocationalTempDTO.getCreateTime());
            map.put("updateTime", employeeVocationalTempDTO.getUpdateTime());
            map.put("createBy", employeeVocationalTempDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void check(PmEmployeeVocationalTemp employeeVocationalTempNew) {
        //校核
        pmEmployeeVocationalTempDao.updateCheckFlag(employeeVocationalTempNew);
        PmEmployeeVocationalTemp vocationalTemp = pmEmployeeVocationalTempDao.getByKey(employeeVocationalTempNew.getId());
        if ("Y".equals(employeeVocationalTempNew.getCheckFlag())) {
            PmEmployeeVocational pmEmployeeVocational = pmEmployeeVocationalCheckMapper.toEntity(vocationalTemp);
            if ("A".equals(vocationalTemp.getInstructionsFlag())) {
                pmEmployeeVocationalDao.insertAllColumn(pmEmployeeVocational);
            } else if ("U".equals(vocationalTemp.getInstructionsFlag())) {
                pmEmployeeVocationalDao.updateAllColumnByKey(pmEmployeeVocational);
            }
        }
    }
}
