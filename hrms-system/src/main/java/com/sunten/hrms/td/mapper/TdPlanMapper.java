package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdPlanDTO;
import com.sunten.hrms.td.domain.TdPlan;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-05-19
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdPlanMapper extends BaseMapper<TdPlanDTO, TdPlan> {

}
