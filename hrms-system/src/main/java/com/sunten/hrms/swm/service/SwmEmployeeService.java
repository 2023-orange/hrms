package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmEmployee;
import com.sunten.hrms.swm.dto.SwmEmployeeDTO;
import com.sunten.hrms.swm.dto.SwmEmployeeQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.swm.vo.EmployeeDistinctTings;
import com.sunten.hrms.swm.vo.EmployeeMsg;
import com.sunten.hrms.swm.vo.JobTransferSalaryVo;
import com.sunten.hrms.swm.vo.SwmDeptVo;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>
 * 薪酬员工信息表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmEmployeeService extends IService<SwmEmployee> {

    SwmEmployeeDTO insert(SwmEmployee employeeNew);

    void delete(Long id);

    void delete(SwmEmployee employee);

    void update(SwmEmployee employeeNew);

    SwmEmployeeDTO getByKey(Long id);

    SwmEmployeeDTO getEmployeeByEmployeeid(Long employeeid);

    List<SwmEmployeeDTO> listAll(SwmEmployeeQueryCriteria criteria);

    Map<String, Object> listAll(SwmEmployeeQueryCriteria criteria, Pageable pageable);

    Map<String, Object> listAllHaveAuth(SwmEmployeeQueryCriteria criteria, Pageable pageable);

    void download(List<SwmEmployeeDTO> employeeDTOS, HttpServletResponse response) throws IOException;

    EmployeeDistinctTings getDistinctTings();

    void updateByWorkFlag(SwmEmployee employee);

//    void updateByManagerFlag(SwmEmployee employee);

    List<EmployeeMsg> listForSwmEmpUse(String nameOrWorkCard);

    List<SwmDeptVo> buildDeptList();

    SwmEmployeeDTO getSwmEmpByEmpId();

    // 部门
    List<String> getDistDepartment();
    // 科室
    List<String> getDistAdministrativeOffice(String department);
    // 班组
    List<String> getTeamList(String office);

    void insertUserRoleBySwmEmp(SwmEmployee swmEmployee);

    void interfaceToMain(Long groupId);

    void updateSeniorityAllowanceMonthly();

    JobTransferSalaryVo getTransforFromSalary(Long employeeId);

    int getSalaryStatus();

    void autoUpdateEducationTittle();
}
