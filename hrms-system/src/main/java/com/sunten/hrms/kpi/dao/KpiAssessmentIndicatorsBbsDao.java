package com.sunten.hrms.kpi.dao;

import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsBbs;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsBbsQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-12-26
 */
@Mapper
@Repository
public interface KpiAssessmentIndicatorsBbsDao extends BaseMapper<KpiAssessmentIndicatorsBbs> {

    int insertAllColumn(KpiAssessmentIndicatorsBbs assessmentIndicatorsBbs);

    int deleteByKey();

    int deleteByEntityKey(KpiAssessmentIndicatorsBbs assessmentIndicatorsBbs);

    int updateAllColumnByKey(KpiAssessmentIndicatorsBbs assessmentIndicatorsBbs);

    KpiAssessmentIndicatorsBbs getByKey();

    List<KpiAssessmentIndicatorsBbs> listAllByCriteria(@Param(value = "criteria") KpiAssessmentIndicatorsBbsQueryCriteria criteria);

    List<KpiAssessmentIndicatorsBbs> listAllByCriteriaPage(@Param(value = "page") Page<KpiAssessmentIndicatorsBbs> page, @Param(value = "criteria") KpiAssessmentIndicatorsBbsQueryCriteria criteria);
}
