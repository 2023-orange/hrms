package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmDistributionMethod;
import com.sunten.hrms.swm.dto.SwmDistributionMethodQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 分配方式 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmDistributionMethodDao extends BaseMapper<SwmDistributionMethod> {

    int insertAllColumn(SwmDistributionMethod distributionMethod);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmDistributionMethod distributionMethod);

    int updateAllColumnByKey(SwmDistributionMethod distributionMethod);

    SwmDistributionMethod getByKey(@Param(value = "id") Long id);

    List<SwmDistributionMethod> listAllByCriteria(@Param(value = "criteria") SwmDistributionMethodQueryCriteria criteria);

    List<SwmDistributionMethod> listAllByCriteriaPage(@Param(value = "page") Page<SwmDistributionMethod> page, @Param(value = "criteria") SwmDistributionMethodQueryCriteria criteria);

    Integer checkName(@Param(value = "distributionMethod") String distributionMethod, @Param(value = "generationDifferentiationFlag") Boolean generationDifferentiationFlag);

    int updateByEnabled(SwmDistributionMethod distributionMethod);
}
