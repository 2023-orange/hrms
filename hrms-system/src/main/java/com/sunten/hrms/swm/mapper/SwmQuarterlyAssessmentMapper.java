package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmQuarterlyAssessmentDTO;
import com.sunten.hrms.swm.domain.SwmQuarterlyAssessment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmQuarterlyAssessmentMapper extends BaseMapper<SwmQuarterlyAssessmentDTO, SwmQuarterlyAssessment> {

}
