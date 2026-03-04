package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdPlanChangeHistory;
import com.sunten.hrms.td.dto.TdPlanChangeHistoryQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训计划变更历史 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-06-16
 */
@Mapper
@Repository
public interface TdPlanChangeHistoryDao extends BaseMapper<TdPlanChangeHistory> {

    int insertAllColumn(TdPlanChangeHistory planChangeHistory);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdPlanChangeHistory planChangeHistory);

    int updateAllColumnByKey(TdPlanChangeHistory planChangeHistory);

    TdPlanChangeHistory getByKey(@Param(value = "id") Long id);

    List<TdPlanChangeHistory> listAllByCriteria(@Param(value = "criteria") TdPlanChangeHistoryQueryCriteria criteria);

    List<TdPlanChangeHistory> listAllByCriteriaPage(@Param(value = "page") Page<TdPlanChangeHistory> page, @Param(value = "criteria") TdPlanChangeHistoryQueryCriteria criteria);

    int updatePassOrNotPass(TdPlanChangeHistory planChangeHistory);
}
