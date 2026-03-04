package com.sunten.hrms.kpi.dao;

import com.sunten.hrms.kpi.domain.KpiAssessmentDimensionDept;
import com.sunten.hrms.kpi.dto.KpiAssessmentDimensionDeptQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * KPI考核维度与部门关系表 Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-28
 */
@Mapper
@Repository
public interface KpiAssessmentDimensionDeptDao extends BaseMapper<KpiAssessmentDimensionDept> {

    int insertAllColumn(KpiAssessmentDimensionDept assessmentDimensionDept);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(KpiAssessmentDimensionDept assessmentDimensionDept);

    int updateAllColumnByKey(KpiAssessmentDimensionDept assessmentDimensionDept);

    KpiAssessmentDimensionDept getByKey(@Param(value = "id") Long id);

    List<KpiAssessmentDimensionDept> listAllByCriteria(@Param(value = "criteria") KpiAssessmentDimensionDeptQueryCriteria criteria);

    List<KpiAssessmentDimensionDept> listAllByCriteriaPage(@Param(value = "page") Page<KpiAssessmentDimensionDept> page, @Param(value = "criteria") KpiAssessmentDimensionDeptQueryCriteria criteria);

    List<KpiAssessmentDimensionDept> listMultipleChoice(@Param(value = "id") Long id);

    List<Long> listMultipleChoiceLongList(@Param(value = "id") Long id);

    int deleteByKpiTree(KpiAssessmentDimensionDept assessmentDimensionDept);
}
