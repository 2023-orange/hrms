package com.sunten.hrms.swm.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.swm.dao.SwmDistributionMethodDao;
import com.sunten.hrms.swm.domain.SwmDistributionMethod;
import com.sunten.hrms.swm.domain.SwmDistributionMethodDepartment;
import com.sunten.hrms.swm.dao.SwmDistributionMethodDepartmentDao;
import com.sunten.hrms.swm.service.SwmDistributionMethodDepartmentService;
import com.sunten.hrms.swm.dto.SwmDistributionMethodDepartmentDTO;
import com.sunten.hrms.swm.dto.SwmDistributionMethodDepartmentQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmDistributionMethodDepartmentMapper;
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
 * 分配方式部门科室关联表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmDistributionMethodDepartmentServiceImpl extends ServiceImpl<SwmDistributionMethodDepartmentDao, SwmDistributionMethodDepartment> implements SwmDistributionMethodDepartmentService {
    private final SwmDistributionMethodDepartmentDao swmDistributionMethodDepartmentDao;
    private final SwmDistributionMethodDepartmentMapper swmDistributionMethodDepartmentMapper;
    private final SwmDistributionMethodDao swmDistributionMethodDao;

    public SwmDistributionMethodDepartmentServiceImpl(SwmDistributionMethodDepartmentDao swmDistributionMethodDepartmentDao, SwmDistributionMethodDepartmentMapper swmDistributionMethodDepartmentMapper
    ,SwmDistributionMethodDao swmDistributionMethodDao) {
        this.swmDistributionMethodDepartmentDao = swmDistributionMethodDepartmentDao;
        this.swmDistributionMethodDepartmentMapper = swmDistributionMethodDepartmentMapper;
        this.swmDistributionMethodDao = swmDistributionMethodDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmDistributionMethodDepartmentDTO insert(SwmDistributionMethodDepartment distributionMethodDepartmentNew) {
        SwmDistributionMethodDepartmentQueryCriteria swmDistributionMethodDepartmentQueryCriteria = new SwmDistributionMethodDepartmentQueryCriteria();
        if (distributionMethodDepartmentNew.getDeptCode().contains(".")) {
            String[] s = distributionMethodDepartmentNew.getDeptCode().split("\\.");
            swmDistributionMethodDepartmentQueryCriteria.setDepartment(s[0]);
            swmDistributionMethodDepartmentQueryCriteria.setAdministrativeOffice(s[1]);
            distributionMethodDepartmentNew.setDepartment(s[0]);
            distributionMethodDepartmentNew.setAdministrativeOffice(s[1]);
        } else {
            swmDistributionMethodDepartmentQueryCriteria.setDepartment(distributionMethodDepartmentNew.getDeptCode());
            distributionMethodDepartmentNew.setDepartment(distributionMethodDepartmentNew.getDeptCode());
        }
        // 获取分配方式

        List<SwmDistributionMethodDepartment> swmDistributionMethodDepartments = swmDistributionMethodDepartmentDao.listAllByCriteria(swmDistributionMethodDepartmentQueryCriteria);

        if (swmDistributionMethodDepartments.size() == 0) {
            swmDistributionMethodDepartmentDao.insertAllColumn(distributionMethodDepartmentNew);
            return swmDistributionMethodDepartmentMapper.toDto(distributionMethodDepartmentNew);
        }
        if (swmDistributionMethodDepartments.size() == 1) {
            SwmDistributionMethod swmDistributionMethod = swmDistributionMethodDao.getByKey(distributionMethodDepartmentNew.getDistributionMethodId());
            if (!swmDistributionMethodDepartments.get(0).getSwmDistributionMethod().getDistributionMethod().equals(swmDistributionMethod.getDistributionMethod())) {
                throw new InfoCheckWarningMessException("该节点已存在分配方式，你只能选择相同的分配方式且不同的生产区分");
            } else {
                if (swmDistributionMethodDepartments.get(0).getSwmDistributionMethod().getGenerationDifferentiationFlag() == swmDistributionMethod.getGenerationDifferentiationFlag()) {
                    throw new InfoCheckWarningMessException("该节点已存在分配方式，你只能选择相同的分配方式且不同的生产区分");
                }
            }
            // 执行insert
            swmDistributionMethodDepartmentDao.insertAllColumn(distributionMethodDepartmentNew);
            return swmDistributionMethodDepartmentMapper.toDto(distributionMethodDepartmentNew);
        }

        throw new InfoCheckWarningMessException("该节点已存在同名非生产与生产的分配方式，不允许继续绑定新的分配方式");


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmDistributionMethodDepartment distributionMethodDepartment = new SwmDistributionMethodDepartment();
        distributionMethodDepartment.setId(id);
        this.delete(distributionMethodDepartment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmDistributionMethodDepartment distributionMethodDepartment) {
        // TODO    确认删除前是否需要做检查
        swmDistributionMethodDepartmentDao.deleteByEntityKey(distributionMethodDepartment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmDistributionMethodDepartment distributionMethodDepartmentNew) {
        SwmDistributionMethodDepartmentQueryCriteria swmDistributionMethodDepartmentQueryCriteria = new SwmDistributionMethodDepartmentQueryCriteria();
            swmDistributionMethodDepartmentQueryCriteria.setDepartment(distributionMethodDepartmentNew.getDepartment());
        if (null != distributionMethodDepartmentNew.getAdministrativeOffice()) {
            swmDistributionMethodDepartmentQueryCriteria.setAdministrativeOffice(distributionMethodDepartmentNew.getAdministrativeOffice());
        }
        // 获取数据中
        List<SwmDistributionMethodDepartment> swmDistributionMethodDepartments = swmDistributionMethodDepartmentDao.listAllByCriteria(swmDistributionMethodDepartmentQueryCriteria);
        if (swmDistributionMethodDepartments.size() == 1) { // 数量1 时直接更新
            SwmDistributionMethodDepartment distributionMethodDepartmentInDb = Optional.ofNullable(swmDistributionMethodDepartmentDao.getByKey(distributionMethodDepartmentNew.getId())).orElseGet(SwmDistributionMethodDepartment::new);
            ValidationUtil.isNull(distributionMethodDepartmentInDb.getId() ,"DistributionMethodDepartment", "id", distributionMethodDepartmentNew.getId());
            distributionMethodDepartmentNew.setId(distributionMethodDepartmentInDb.getId());
            swmDistributionMethodDepartmentDao.updateAllColumnByKey(distributionMethodDepartmentNew);
        }
        if (swmDistributionMethodDepartments.size() == 2) {
            throw new InfoCheckWarningMessException("该部门科室已存在生产及非生产的分配方式，不能作任何修改，只允许删除");
        }
    }

    @Override
    public SwmDistributionMethodDepartmentDTO getByKey(Long id) {
        SwmDistributionMethodDepartment distributionMethodDepartment = Optional.ofNullable(swmDistributionMethodDepartmentDao.getByKey(id)).orElseGet(SwmDistributionMethodDepartment::new);
        ValidationUtil.isNull(distributionMethodDepartment.getId() ,"DistributionMethodDepartment", "id", id);
        return swmDistributionMethodDepartmentMapper.toDto(distributionMethodDepartment);
    }

    @Override
    public List<SwmDistributionMethodDepartmentDTO> listAll(SwmDistributionMethodDepartmentQueryCriteria criteria) {
        return swmDistributionMethodDepartmentMapper.toDto(swmDistributionMethodDepartmentDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmDistributionMethodDepartmentQueryCriteria criteria, Pageable pageable) {
        Page<SwmDistributionMethodDepartment> page = PageUtil.startPage(pageable);
        List<SwmDistributionMethodDepartment> distributionMethodDepartments = swmDistributionMethodDepartmentDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmDistributionMethodDepartmentMapper.toDto(distributionMethodDepartments), page.getTotal());
    }

    @Override
    public void download(List<SwmDistributionMethodDepartmentDTO> distributionMethodDepartmentDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SwmDistributionMethodDepartmentDTO distributionMethodDepartmentDTO : distributionMethodDepartmentDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("部门", distributionMethodDepartmentDTO.getDepartment());
            map.put("科室", distributionMethodDepartmentDTO.getAdministrativeOffice());
            map.put("分配方式", distributionMethodDepartmentDTO.getSwmDistributionMethod().getDistributionMethod() + (distributionMethodDepartmentDTO.getSwmDistributionMethod().getGenerationDifferentiationFlag() ? ",生产" : ",非生产"));
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
