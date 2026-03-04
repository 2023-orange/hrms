package com.sunten.hrms.kpi.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsInterfaceDTO;
import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsInterface;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zhoujy
 * @since 2023-12-20
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KpiAssessmentIndicatorsInterfaceMapper extends BaseMapper<KpiAssessmentIndicatorsInterfaceDTO, KpiAssessmentIndicatorsInterface> {

}
