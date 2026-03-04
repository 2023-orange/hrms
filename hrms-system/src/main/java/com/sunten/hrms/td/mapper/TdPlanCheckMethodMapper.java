package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdPlanCheckMethodDTO;
import com.sunten.hrms.td.domain.TdPlanCheckMethod;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2022-03-08
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdPlanCheckMethodMapper extends BaseMapper<TdPlanCheckMethodDTO, TdPlanCheckMethod> {

}
