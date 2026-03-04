package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeJob;
import com.sunten.hrms.pm.dto.PmEmployeeJobQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 部门科室岗位关系表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeJobDao extends BaseMapper<PmEmployeeJob> {

    int insertAllColumn(PmEmployeeJob employeeJob);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeJob employeeJob);

    int updateAllColumnByKey(PmEmployeeJob employeeJob);

    int updateEnableFlagByKey(PmEmployeeJob employeeJob);

    PmEmployeeJob getByKey(@Param(value = "id") Long id);

    PmEmployeeJob getMainJObByKey(@Param(value = "employeeId") Long employeeId);

    List<PmEmployeeJob> checkHavePm(@Param(value = "criteria") PmEmployeeJobQueryCriteria criteria);

    List<PmEmployeeJob> listAllByCriteria(@Param(value = "criteria") PmEmployeeJobQueryCriteria criteria);

    List<PmEmployeeJob> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeJob> page, @Param(value = "criteria") PmEmployeeJobQueryCriteria criteria);

    int updateMainFlag(PmEmployeeJob employeeJob);

    PmEmployeeJob getByEmployeeJob(PmEmployeeJob employeeJob);

    int updateDeptName(PmEmployeeJob employeeJob);

    int updateJobName(PmEmployeeJob employeeJob);

    Long getMaxGroupId();

    List<PmEmployeeJob> listByEmpIdAndEnabledFlagWithExtend(@Param(value = "employeeId") Long employeeId, @Param(value = "enabledFlag") boolean enabledFlag);

    int updateAllMainFlag(PmEmployeeJob employeeJob);

    List<PmEmployeeJob> listByJobId(@Param(value = "jobId") Long jobId);

    List<PmEmployeeJob> listAllEntityDeptIds(@Param(value = "criteria") PmEmployeeJobQueryCriteria criteria);

    List<PmEmployeeJob> listJobEmployee(@Param(value = "jobId") Long jobId, @Param(value = "enabledFlag") boolean enabledFlag);

    Set<String> listJobEmployeeByName(@Param(value = "jobName") String jobName);

    PmEmployeeJob getManagerOrSupervisor(@Param(value = "userId") Long userId);

    List<PmEmployeeJob> superQuery(@Param(value = "queryValue") String queryValue);
}
