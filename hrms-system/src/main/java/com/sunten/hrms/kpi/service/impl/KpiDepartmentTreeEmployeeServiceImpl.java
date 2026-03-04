package com.sunten.hrms.kpi.service.impl;

import com.sunten.hrms.kpi.domain.KpiDepartmentTreeEmployee;
import com.sunten.hrms.kpi.dao.KpiDepartmentTreeEmployeeDao;
import com.sunten.hrms.kpi.service.KpiDepartmentTreeEmployeeService;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeEmployeeDTO;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeEmployeeQueryCriteria;
import com.sunten.hrms.kpi.mapper.KpiDepartmentTreeEmployeeMapper;
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
 * KPI资料填写人中间表 服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KpiDepartmentTreeEmployeeServiceImpl extends ServiceImpl<KpiDepartmentTreeEmployeeDao, KpiDepartmentTreeEmployee> implements KpiDepartmentTreeEmployeeService {
    private final KpiDepartmentTreeEmployeeDao kpiDepartmentTreeEmployeeDao;
    private final KpiDepartmentTreeEmployeeMapper kpiDepartmentTreeEmployeeMapper;

    public KpiDepartmentTreeEmployeeServiceImpl(KpiDepartmentTreeEmployeeDao kpiDepartmentTreeEmployeeDao, KpiDepartmentTreeEmployeeMapper kpiDepartmentTreeEmployeeMapper) {
        this.kpiDepartmentTreeEmployeeDao = kpiDepartmentTreeEmployeeDao;
        this.kpiDepartmentTreeEmployeeMapper = kpiDepartmentTreeEmployeeMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KpiDepartmentTreeEmployeeDTO insert(KpiDepartmentTreeEmployee departmentTreeEmployeeNew) {
        kpiDepartmentTreeEmployeeDao.insertAllColumn(departmentTreeEmployeeNew);
        return kpiDepartmentTreeEmployeeMapper.toDto(departmentTreeEmployeeNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        KpiDepartmentTreeEmployee departmentTreeEmployee = new KpiDepartmentTreeEmployee();
        departmentTreeEmployee.setId(id);
        this.delete(departmentTreeEmployee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(KpiDepartmentTreeEmployee departmentTreeEmployee) {
        // TODO    确认删除前是否需要做检查
        kpiDepartmentTreeEmployeeDao.deleteByEntityKey(departmentTreeEmployee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KpiDepartmentTreeEmployee departmentTreeEmployeeNew) {
        KpiDepartmentTreeEmployee departmentTreeEmployeeInDb = Optional.ofNullable(kpiDepartmentTreeEmployeeDao.getByKey(departmentTreeEmployeeNew.getId())).orElseGet(KpiDepartmentTreeEmployee::new);
        ValidationUtil.isNull(departmentTreeEmployeeInDb.getId() ,"DepartmentTreeEmployee", "id", departmentTreeEmployeeNew.getId());
        departmentTreeEmployeeNew.setId(departmentTreeEmployeeInDb.getId());
        kpiDepartmentTreeEmployeeDao.updateAllColumnByKey(departmentTreeEmployeeNew);
    }

    @Override
    public KpiDepartmentTreeEmployeeDTO getByKey(Long id) {
        KpiDepartmentTreeEmployee departmentTreeEmployee = Optional.ofNullable(kpiDepartmentTreeEmployeeDao.getByKey(id)).orElseGet(KpiDepartmentTreeEmployee::new);
        ValidationUtil.isNull(departmentTreeEmployee.getId() ,"DepartmentTreeEmployee", "id", id);
        return kpiDepartmentTreeEmployeeMapper.toDto(departmentTreeEmployee);
    }

    @Override
    public List<KpiDepartmentTreeEmployeeDTO> listAll(KpiDepartmentTreeEmployeeQueryCriteria criteria) {
        return kpiDepartmentTreeEmployeeMapper.toDto(kpiDepartmentTreeEmployeeDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(KpiDepartmentTreeEmployeeQueryCriteria criteria, Pageable pageable) {
        Page<KpiDepartmentTreeEmployee> page = PageUtil.startPage(pageable);
        List<KpiDepartmentTreeEmployee> departmentTreeEmployees = kpiDepartmentTreeEmployeeDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(kpiDepartmentTreeEmployeeMapper.toDto(departmentTreeEmployees), page.getTotal());
    }

    @Override
    public void download(List<KpiDepartmentTreeEmployeeDTO> departmentTreeEmployeeDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (KpiDepartmentTreeEmployeeDTO departmentTreeEmployeeDTO : departmentTreeEmployeeDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("主键", departmentTreeEmployeeDTO.getId());
            map.put("资料填写人编号", departmentTreeEmployeeDTO.getEmployeeId());
            map.put("部门编号", departmentTreeEmployeeDTO.getDepartmentTreeId());
            map.put("弹性域1", departmentTreeEmployeeDTO.getAttribute1());
            map.put("弹性域2", departmentTreeEmployeeDTO.getAttribute2());
            map.put("弹性域3", departmentTreeEmployeeDTO.getAttribute3());
            map.put("弹性域4", departmentTreeEmployeeDTO.getAttribute4());
            map.put("弹性域5", departmentTreeEmployeeDTO.getAttribute5());
            map.put("创建时间", departmentTreeEmployeeDTO.getCreateTime());
            map.put("创建人", departmentTreeEmployeeDTO.getCreateBy());
            map.put("更新时间", departmentTreeEmployeeDTO.getUpdateTime());
            map.put("更新人", departmentTreeEmployeeDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
