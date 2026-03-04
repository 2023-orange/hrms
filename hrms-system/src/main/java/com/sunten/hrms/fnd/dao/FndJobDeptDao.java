package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndJobAndDept;
import com.sunten.hrms.fnd.domain.FndJobDept;
import com.sunten.hrms.fnd.dto.FndJobDeptQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-12-03
 */
@Mapper
@Repository
public interface FndJobDeptDao extends BaseMapper<FndJobDept> {

    int insertAllColumn(FndJobDept jobDept);

    int deleteByKey(@Param(value = "deptId") Long deptId, @Param(value = "jobId") Long jobId);

    int deleteByEntityKey(FndJobDept jobDept);

    int updateAllColumnByKey(FndJobDept jobDept);

    FndJobDept getByKey(@Param(value = "deptId") Long deptId, @Param(value = "jobId") Long jobId);

    List<FndJobDept> listAllByCriteria(@Param(value = "criteria") FndJobDeptQueryCriteria criteria);

    List<FndJobDept> listAllByCriteriaPage(@Param(value = "page") Page<FndJobDept> page, @Param(value = "criteria") FndJobDeptQueryCriteria criteria);

    int deleteByJobId(@Param(value = "jobId") Long jobId);

    String listDeptNameById(@Param(value = "Id") Long Id);
}
