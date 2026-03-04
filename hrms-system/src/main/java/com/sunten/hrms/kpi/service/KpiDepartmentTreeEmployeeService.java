package com.sunten.hrms.kpi.service;

import com.sunten.hrms.kpi.domain.KpiDepartmentTreeEmployee;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeEmployeeDTO;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeEmployeeQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * KPI资料填写人中间表 服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
public interface KpiDepartmentTreeEmployeeService extends IService<KpiDepartmentTreeEmployee> {

    KpiDepartmentTreeEmployeeDTO insert(KpiDepartmentTreeEmployee departmentTreeEmployeeNew);

    void delete(Long id);

    void delete(KpiDepartmentTreeEmployee departmentTreeEmployee);

    void update(KpiDepartmentTreeEmployee departmentTreeEmployeeNew);

    KpiDepartmentTreeEmployeeDTO getByKey(Long id);

    List<KpiDepartmentTreeEmployeeDTO> listAll(KpiDepartmentTreeEmployeeQueryCriteria criteria);

    Map<String, Object> listAll(KpiDepartmentTreeEmployeeQueryCriteria criteria, Pageable pageable);

    void download(List<KpiDepartmentTreeEmployeeDTO> departmentTreeEmployeeDTOS, HttpServletResponse response) throws IOException;
}
