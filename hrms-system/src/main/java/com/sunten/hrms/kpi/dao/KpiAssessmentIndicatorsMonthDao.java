package com.sunten.hrms.kpi.dao;

import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsMonth;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsMonthQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * KPI考核指标子表 Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-28
 */
@Mapper
@Repository
public interface KpiAssessmentIndicatorsMonthDao extends BaseMapper<KpiAssessmentIndicatorsMonth> {

    int insertAllColumn(KpiAssessmentIndicatorsMonth assessmentIndicatorsMonth);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(KpiAssessmentIndicatorsMonth assessmentIndicatorsMonth);

    int updateAllColumnByKey(KpiAssessmentIndicatorsMonth assessmentIndicatorsMonth);

    KpiAssessmentIndicatorsMonth getByKey(@Param(value = "id") Long id);

    List<KpiAssessmentIndicatorsMonth> listAllByCriteria(@Param(value = "criteria") KpiAssessmentIndicatorsMonthQueryCriteria criteria);

    List<KpiAssessmentIndicatorsMonth> listAllByCriteriaPage(@Param(value = "page") Page<KpiAssessmentIndicatorsMonth> page, @Param(value = "criteria") KpiAssessmentIndicatorsMonthQueryCriteria criteria);
}
