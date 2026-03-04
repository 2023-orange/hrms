package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmCostCenter;
import com.sunten.hrms.swm.dto.SwmCostCenterQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 成本中心表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmCostCenterDao extends BaseMapper<SwmCostCenter> {

    int insertAllColumn(SwmCostCenter costCenter);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmCostCenter costCenter);

    int updateAllColumnByKey(SwmCostCenter costCenter);

    SwmCostCenter getByKey(@Param(value = "id") Long id);

    List<SwmCostCenter> listAllByCriteria(@Param(value = "criteria") SwmCostCenterQueryCriteria criteria);

    List<SwmCostCenter> listAllByCriteriaPage(@Param(value = "page") Page<SwmCostCenter> page, @Param(value = "criteria") SwmCostCenterQueryCriteria criteria);
}
