package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmFloatingWageInterface;
import com.sunten.hrms.swm.dto.SwmFloatingWageInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 浮动工资接口表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmFloatingWageInterfaceDao extends BaseMapper<SwmFloatingWageInterface> {

    int insertAllColumn(SwmFloatingWageInterface floatingWageInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmFloatingWageInterface floatingWageInterface);

    int updateAllColumnByKey(SwmFloatingWageInterface floatingWageInterface);

    SwmFloatingWageInterface getByKey(@Param(value = "id") Long id);

    List<SwmFloatingWageInterface> listAllByCriteria(@Param(value = "criteria") SwmFloatingWageInterfaceQueryCriteria criteria);

    List<SwmFloatingWageInterface> listAllByCriteriaPage(@Param(value = "page") Page<SwmFloatingWageInterface> page, @Param(value = "criteria") SwmFloatingWageInterfaceQueryCriteria criteria);

    int insertAllocatePerformancePay(SwmFloatingWageInterface swmFloatingWageInterface);

    List<SwmFloatingWageInterface> getSummaryByImportList(@Param(value = "incomePeriod")String incomePeriod, @Param(value = "workCards")Set<String> workCards, @Param(value = "groupIds")Set<Long> groupIds);
}
