package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmQuarterlyAssessmentInterfaceDTO;
import com.sunten.hrms.swm.domain.SwmQuarterlyAssessmentInterface;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2022-05-13
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmQuarterlyAssessmentInterfaceMapper extends BaseMapper<SwmQuarterlyAssessmentInterfaceDTO, SwmQuarterlyAssessmentInterface> {

}
