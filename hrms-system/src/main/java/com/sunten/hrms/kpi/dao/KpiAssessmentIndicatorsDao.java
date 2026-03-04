package com.sunten.hrms.kpi.dao;

import com.sunten.hrms.kpi.domain.KpiAssessmentIndicators;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * KPI考核指标表 Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-28
 */
@Mapper
@Repository
public interface KpiAssessmentIndicatorsDao extends BaseMapper<KpiAssessmentIndicators> {

    int insertAllColumn(KpiAssessmentIndicators assessmentIndicators);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(KpiAssessmentIndicators assessmentIndicators);

    int updateAllColumnByKey(KpiAssessmentIndicators assessmentIndicators);

    KpiAssessmentIndicators getByKey(@Param(value = "id") Long id);

    List<KpiAssessmentIndicators> listAllByCriteria(@Param(value = "criteria") KpiAssessmentIndicatorsQueryCriteria criteria);

    List<KpiAssessmentIndicators> listAllByCriteriaPage(@Param(value = "page") Page<KpiAssessmentIndicators> page, @Param(value = "criteria") KpiAssessmentIndicatorsQueryCriteria criteria);

    List<KpiAssessmentIndicators> listAssessedIndicators(@Param(value = "criteria") KpiAssessmentIndicatorsQueryCriteria criteria);

    List<KpiAssessmentIndicators> listAssessedIndicatorsPage(@Param(value = "page") Page<KpiAssessmentIndicators> page, @Param(value = "criteria") KpiAssessmentIndicatorsQueryCriteria criteria);

    Integer getMaxYear();

    // 根据最大年份获取改年份的最大流水号
    Integer getMaxSerialNumberByYear(Integer year);

    // 调用存储过程生成流水号
    Long createSerialNumber(Map<String, Object> map);

    Boolean getAssessmentIndicatorsAddFlag(String workCard);


    List<HashMap<String, Object>> getTreeListInfo(@Param("year") String year);

    List<HashMap<String, Object>> getKpiAssessmentIndicators(@Param("Id") Long id, @Param("year")String year, @Param("contextType")String contextType);

    List<HashMap<String, Object>> getSuperAssessmentIndicators(@Param("Id") Long id, @Param("year")String year, @Param("contextType")String contextType);


    List<HashMap<String, Object>> getTreeListInfoByManger(String year);

    Double getResidueWeight(Long id);
}
