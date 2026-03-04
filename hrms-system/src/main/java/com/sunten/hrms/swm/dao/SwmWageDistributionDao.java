package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmWageDistribution;
import com.sunten.hrms.swm.dto.SwmWageDistributionQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工资分配（工资系数）表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmWageDistributionDao extends BaseMapper<SwmWageDistribution> {

    int insertAllColumn(SwmWageDistribution wageDistribution);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmWageDistribution wageDistribution);

    int updateAllColumnByKey(SwmWageDistribution wageDistribution);

    SwmWageDistribution getByKey(@Param(value = "id") Long id);

    List<SwmWageDistribution> listAllByCriteria(@Param(value = "criteria") SwmWageDistributionQueryCriteria criteria);

    List<SwmWageDistribution> listAllByCriteriaPage(@Param(value = "page") Page<SwmWageDistribution> page, @Param(value = "criteria") SwmWageDistributionQueryCriteria criteria);

    void generateWageDistributionByMsp(Map<String, Object> map);

    int deleteByPeriod(@Param(value = "period")String period);

    Integer countByPeriod(@Param(value = "period")String period);
}
