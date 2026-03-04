package com.sunten.hrms.kpi.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsBbsDTO;
import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsBbs;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zhoujy
 * @since 2023-12-26
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KpiAssessmentIndicatorsBbsMapper extends BaseMapper<KpiAssessmentIndicatorsBbsDTO, KpiAssessmentIndicatorsBbs> {

}
