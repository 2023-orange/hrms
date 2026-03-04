package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdTrainEmployeeJurisdiction;
import com.sunten.hrms.td.dto.TdTrainEmployeeJurisdictionQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训员权限表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-06-23
 */
@Mapper
@Repository
public interface TdTrainEmployeeJurisdictionDao extends BaseMapper<TdTrainEmployeeJurisdiction> {

    int insertAllColumn(TdTrainEmployeeJurisdiction trainEmployeeJurisdiction);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdTrainEmployeeJurisdiction trainEmployeeJurisdiction);

    int updateAllColumnByKey(TdTrainEmployeeJurisdiction trainEmployeeJurisdiction);

    TdTrainEmployeeJurisdiction getByKey(@Param(value = "id") Long id);

    List<TdTrainEmployeeJurisdiction> listAllByCriteria(@Param(value = "criteria") TdTrainEmployeeJurisdictionQueryCriteria criteria);

    List<TdTrainEmployeeJurisdiction> listAllByCriteriaPage(@Param(value = "page") Page<TdTrainEmployeeJurisdiction> page, @Param(value = "criteria") TdTrainEmployeeJurisdictionQueryCriteria criteria);

    List<Long> getDeptsByEmployeeeId(@Param(value = "employeeId") Long employeeId);

    int deleteByEmployeeAndDept(@Param(value = "employeeId") Long employeeId, @Param(value = "deptId") Long deptId);
    // 删除该员工的所有管辖部门
    int deleteByEmployee(@Param(value = "employeeId") Long employeeId);
    // 根据管辖该部门的培训员
    TdTrainEmployeeJurisdiction getByDept(@Param(value = "deptId") Long deptId);
}
