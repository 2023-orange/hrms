package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmDept;
import com.sunten.hrms.swm.domain.SwmEmployee;
import com.sunten.hrms.swm.dto.SwmEmployeeQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.swm.vo.JobTransferSalaryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 薪酬员工信息表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmEmployeeDao extends BaseMapper<SwmEmployee> {

    int insertAllColumn(SwmEmployee employee);

    int deleteByKey(@Param(value = "id") Long id);

    int updateByWorkFlag(SwmEmployee employee);

    int deleteByEntityKey(SwmEmployee employee);

    int updateAllColumnByKey(SwmEmployee employee);

    int updateByManagerFlag(SwmEmployee employee);

    SwmEmployee getByKey(@Param(value = "id") Long id);

    SwmEmployee getByEmpId(@Param(value = "empId") Long id);

    List<SwmEmployee> listAllByCriteria(@Param(value = "criteria") SwmEmployeeQueryCriteria criteria);

    List<SwmEmployee> listAllHaveAuthByCriteriaPage(@Param(value = "page") Page<SwmEmployee> page, @Param(value = "criteria") SwmEmployeeQueryCriteria criteria);

    List<SwmEmployee> listAllByCriteriaPage(@Param(value = "page") Page<SwmEmployee> page, @Param(value = "criteria") SwmEmployeeQueryCriteria criteria);

    List<String> distinctByDepartment();

    List<String> distinctByAdministrativeOffice(@Param(value = "department") String department);

    List<String> distinctByTeam(@Param(value = "office") String office);

    List<String> distinctByStation();

    List<String> distinctByEmployeeCategory();

    List<String> distinctByRank();

    List<String> distinctByTechnicalRank();

    List<String> distinctBySkillLevel();

    List<String> distinctByCategory();

    List<String> distinctByJob();

    List<String> distinctByPosition();

    List<String> distinctByTitle();

    List<String> distinctByEducation();

    List<SwmDept> distinctByDept();

    List<String> distinctByServiceDepartment();

    int insertUserRoleBySwmEmp(SwmEmployee swmEmployee);

    SwmEmployee getSwmEmpByEmpId(@Param(value = "employeeId")Long employeeId);

    void interfaceToMain(@Param(value = "groupId")Long groupId);

    void updateSeniorityAllowanceMonthly();

    JobTransferSalaryVo getTransforFromSalary(@Param(value = "employeeId")Long employeeId);

    int getSalaryStatus();

    SwmEmployee getEmployeeByEmployeeid(@Param(value = "employeeid")Long employeeid);

    void autoUpdateEducationTittle();
}
