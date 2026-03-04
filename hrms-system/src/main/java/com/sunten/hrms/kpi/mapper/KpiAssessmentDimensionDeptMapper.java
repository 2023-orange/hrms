package com.sunten.hrms.kpi.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.kpi.dto.KpiAssessmentDimensionDeptDTO;
import com.sunten.hrms.kpi.domain.KpiAssessmentDimensionDept;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zhoujy
 * @since 2023-11-28
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KpiAssessmentDimensionDeptMapper extends BaseMapper<KpiAssessmentDimensionDeptDTO, KpiAssessmentDimensionDept> {

}
