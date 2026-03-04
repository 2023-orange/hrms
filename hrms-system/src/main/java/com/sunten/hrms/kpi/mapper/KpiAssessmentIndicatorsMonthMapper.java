package com.sunten.hrms.kpi.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsMonthDTO;
import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsMonth;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zhoujy
 * @since 2023-11-28
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KpiAssessmentIndicatorsMonthMapper extends BaseMapper<KpiAssessmentIndicatorsMonthDTO, KpiAssessmentIndicatorsMonth> {

}
