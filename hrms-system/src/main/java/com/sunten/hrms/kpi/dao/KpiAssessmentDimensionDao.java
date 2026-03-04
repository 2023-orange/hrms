package com.sunten.hrms.kpi.dao;

import com.sunten.hrms.kpi.domain.KpiAssessmentDimension;
import com.sunten.hrms.kpi.dto.KpiAssessmentDimensionQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * KPI考核维度表 Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
@Mapper
@Repository
public interface KpiAssessmentDimensionDao extends BaseMapper<KpiAssessmentDimension> {

    int insertAllColumn(KpiAssessmentDimension assessmentDimension);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(KpiAssessmentDimension assessmentDimension);

    int updateAllColumnByKey(KpiAssessmentDimension assessmentDimension);

    KpiAssessmentDimension getByKey(@Param(value = "id") Long id);

    List<KpiAssessmentDimension> listAllByCriteria(@Param(value = "criteria") KpiAssessmentDimensionQueryCriteria criteria);

    List<KpiAssessmentDimension> listAllByCriteriaPage(@Param(value = "page") Page<KpiAssessmentDimension> page, @Param(value = "criteria") KpiAssessmentDimensionQueryCriteria criteria);

    List<KpiAssessmentDimension> listAllByEnableFlag();
}
