package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.swm.dao.SwmDistributionMethodDepartmentDao;
import com.sunten.hrms.swm.domain.SwmDistributionMethod;
import com.sunten.hrms.swm.dao.SwmDistributionMethodDao;
import com.sunten.hrms.swm.service.SwmDistributionMethodService;
import com.sunten.hrms.swm.dto.SwmDistributionMethodDTO;
import com.sunten.hrms.swm.dto.SwmDistributionMethodQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmDistributionMethodMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * 分配方式 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmDistributionMethodServiceImpl extends ServiceImpl<SwmDistributionMethodDao, SwmDistributionMethod> implements SwmDistributionMethodService {
    private final SwmDistributionMethodDao swmDistributionMethodDao;
    private final SwmDistributionMethodMapper swmDistributionMethodMapper;
    private final SwmDistributionMethodDepartmentDao swmDistributionMethodDepartmentDao;

    public SwmDistributionMethodServiceImpl(SwmDistributionMethodDao swmDistributionMethodDao, SwmDistributionMethodMapper swmDistributionMethodMapper, SwmDistributionMethodDepartmentDao swmDistributionMethodDepartmentDao) {
        this.swmDistributionMethodDao = swmDistributionMethodDao;
        this.swmDistributionMethodMapper = swmDistributionMethodMapper;
        this.swmDistributionMethodDepartmentDao = swmDistributionMethodDepartmentDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmDistributionMethodDTO insertSwmDistributionMethod(SwmDistributionMethod distributionMethodNew) {
        if (swmDistributionMethodDao.checkName(distributionMethodNew.getDistributionMethod(), distributionMethodNew.getGenerationDifferentiationFlag()) > 0) {
            throw new InfoCheckWarningMessException("该分配方式已存在");
        } else {
            swmDistributionMethodDao.insertAllColumn(distributionMethodNew);
            return swmDistributionMethodMapper.toDto(distributionMethodNew);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmDistributionMethod distributionMethod = new SwmDistributionMethod();
        distributionMethod.setId(id);
        this.delete(distributionMethod);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmDistributionMethod distributionMethod) {
        // TODO    确认删除前是否需要做检查
        swmDistributionMethodDao.deleteByEntityKey(distributionMethod);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmDistributionMethod distributionMethodNew) {
        if (swmDistributionMethodDao.checkName(distributionMethodNew.getDistributionMethod(), distributionMethodNew.getGenerationDifferentiationFlag()) > 0){
            throw new InfoCheckWarningMessException("该分配方式已存在");
        }
        SwmDistributionMethod distributionMethodInDb = Optional.ofNullable(swmDistributionMethodDao.getByKey(distributionMethodNew.getId())).orElseGet(SwmDistributionMethod::new);
        ValidationUtil.isNull(distributionMethodInDb.getId() ,"DistributionMethod", "id", distributionMethodNew.getId());
        distributionMethodNew.setId(distributionMethodInDb.getId());
        swmDistributionMethodDao.updateAllColumnByKey(distributionMethodNew);
    }

    @Override
    public SwmDistributionMethodDTO getByKey(Long id) {
        SwmDistributionMethod distributionMethod = Optional.ofNullable(swmDistributionMethodDao.getByKey(id)).orElseGet(SwmDistributionMethod::new);
        ValidationUtil.isNull(distributionMethod.getId() ,"DistributionMethod", "id", id);
        return swmDistributionMethodMapper.toDto(distributionMethod);
    }

    @Override
    public List<SwmDistributionMethodDTO> listAll(SwmDistributionMethodQueryCriteria criteria) {
        return swmDistributionMethodMapper.toDto(swmDistributionMethodDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmDistributionMethodQueryCriteria criteria, Pageable pageable) {
        Page<SwmDistributionMethod> page = PageUtil.startPage(pageable);
        List<SwmDistributionMethod> distributionMethods = swmDistributionMethodDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmDistributionMethodMapper.toDto(distributionMethods), page.getTotal());
    }

    @Override
    public void download(List<SwmDistributionMethodDTO> distributionMethodDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmDistributionMethodDTO distributionMethodDTO : distributionMethodDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id主键", distributionMethodDTO.getId());
            map.put("分配方式", distributionMethodDTO.getDistributionMethod());
            map.put("生产区分(1为生产，0非生产)", distributionMethodDTO.getGenerationDifferentiationFlag());
            map.put("有效标记（1有效、0无效）", distributionMethodDTO.getEnabledFlag());
            map.put("创建时间", distributionMethodDTO.getCreateTime());
            map.put("创建人id", distributionMethodDTO.getCreateBy());
            map.put("修改时间", distributionMethodDTO.getUpdateTime());
            map.put("修改人id", distributionMethodDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disabled(SwmDistributionMethod distributionMethod) {
        if (swmDistributionMethodDepartmentDao.countEnabled(distributionMethod.getId()) > 0) {
            throw new InfoCheckWarningMessException("该分配方式已被使用，不允许失效");
        } else {
            swmDistributionMethodDao.updateByEnabled(distributionMethod);
        }
    }
}
