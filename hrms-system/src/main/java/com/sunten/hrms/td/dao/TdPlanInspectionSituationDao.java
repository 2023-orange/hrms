package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdPlanInspectionSituation;
import com.sunten.hrms.td.dto.TdPlanInspectionSituationQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训考核情况 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2022-03-11
 */
@Mapper
@Repository
public interface TdPlanInspectionSituationDao extends BaseMapper<TdPlanInspectionSituation> {

    int insertAllColumn(TdPlanInspectionSituation planInspectionSituation);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdPlanInspectionSituation planInspectionSituation);

    int updateAllColumnByKey(TdPlanInspectionSituation planInspectionSituation);

    TdPlanInspectionSituation getByKey(@Param(value = "id") Long id);

    List<TdPlanInspectionSituation> listAllByCriteria(@Param(value = "criteria") TdPlanInspectionSituationQueryCriteria criteria);

    List<TdPlanInspectionSituation> listAllByCriteriaPage(@Param(value = "page") Page<TdPlanInspectionSituation> page, @Param(value = "criteria") TdPlanInspectionSituationQueryCriteria criteria);
}
