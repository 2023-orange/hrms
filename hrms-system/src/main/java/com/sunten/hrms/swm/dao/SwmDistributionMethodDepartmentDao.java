package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmDistributionMethodDepartment;
import com.sunten.hrms.swm.dto.SwmDistributionMethodDepartmentQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 分配方式部门科室关联表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmDistributionMethodDepartmentDao extends BaseMapper<SwmDistributionMethodDepartment> {

    int insertAllColumn(SwmDistributionMethodDepartment distributionMethodDepartment);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmDistributionMethodDepartment distributionMethodDepartment);

    int updateAllColumnByKey(SwmDistributionMethodDepartment distributionMethodDepartment);

    SwmDistributionMethodDepartment getByKey(@Param(value = "id") Long id);

    List<SwmDistributionMethodDepartment> listAllByCriteria(@Param(value = "criteria") SwmDistributionMethodDepartmentQueryCriteria criteria);

    List<SwmDistributionMethodDepartment> listAllByCriteriaPage(@Param(value = "page") Page<SwmDistributionMethodDepartment> page, @Param(value = "criteria") SwmDistributionMethodDepartmentQueryCriteria criteria);

    Integer checkName(@Param(value = "criteria") SwmDistributionMethodDepartmentQueryCriteria criteria);

    Integer countEnabled(@Param(value = "id")Long id);
}
