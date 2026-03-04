package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcEmpDepts;
import com.sunten.hrms.ac.domain.AcOvertimeReview;
import com.sunten.hrms.ac.dto.AcEmpDeptsQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 考勤模块人员数据权限范围表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-12-09
 */
@Mapper
@Repository
public interface AcEmpDeptsDao extends BaseMapper<AcEmpDepts> {

    int insertAllColumn(AcEmpDepts empDepts);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcEmpDepts empDepts);

    int updateAllColumnByKey(AcEmpDepts empDepts);

    AcEmpDepts getByKey(@Param(value = "id") Long id);

    List<AcEmpDepts> listAllByCriteria(@Param(value = "criteria") AcEmpDeptsQueryCriteria criteria);

    List<AcEmpDepts> listAllByCriteriaPage(@Param(value = "page") Page<AcEmpDepts> page, @Param(value = "criteria") AcEmpDeptsQueryCriteria criteria);

    Set<Long> getDeptsByRolePermission(@Param(value = "employeeId") Long employeeId, @Param(value= "dataType") String dataType);

    int insertByOvertimeReview(AcOvertimeReview acOvertimeReview);

    int deleteByEnabled(AcEmpDepts acEmpDepts);

    List<AcEmpDepts> listTempByCriteria(@Param(value = "criteria") AcEmpDeptsQueryCriteria criteria);

    int insertAllColumnTemp(AcEmpDepts empDepts);

    List<PmEmployee> getRoleEmpListByPage(@Param(value = "criteria") PmEmployeeQueryCriteria pmEmployeeQueryCriteria, @Param(value = "page") Page<PmEmployee> page);

    Integer countRoleEmp(@Param(value = "criteria") PmEmployeeQueryCriteria pmEmployeeQueryCriteria);

    void removeRelationByid(@Param(value="employeeId") Long employeeId, @Param(value="dataType") String dataType);

    String getTeamEmailByDeptId(@Param(value="deptId") Long deptId);

    Boolean checkDocPermission(@Param(value = "employeeId") Long employeeId);

    List<AcEmpDepts> listAllByGroup(@Param(value = "criteria") AcEmpDeptsQueryCriteria criteria);

    void insertRoleByAcEmpDept(@Param(value="createBy") Long createBy, @Param(value="permission") String permission,  @Param(value = "userId")Long userId);
}
