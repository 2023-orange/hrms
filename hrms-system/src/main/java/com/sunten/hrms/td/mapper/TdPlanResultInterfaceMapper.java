package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdPlanResultInterfaceDTO;
import com.sunten.hrms.td.domain.TdPlanResultInterface;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-06-17
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdPlanResultInterfaceMapper extends BaseMapper<TdPlanResultInterfaceDTO, TdPlanResultInterface> {

}
