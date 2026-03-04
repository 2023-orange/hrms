package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdPlanInspectionSituationDTO;
import com.sunten.hrms.td.domain.TdPlanInspectionSituation;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2022-03-11
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdPlanInspectionSituationMapper extends BaseMapper<TdPlanInspectionSituationDTO, TdPlanInspectionSituation> {

}
