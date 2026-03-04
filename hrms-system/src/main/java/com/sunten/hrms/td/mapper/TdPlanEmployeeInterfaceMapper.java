package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdPlanEmployeeInterfaceDTO;
import com.sunten.hrms.td.domain.TdPlanEmployeeInterface;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-09-24
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdPlanEmployeeInterfaceMapper extends BaseMapper<TdPlanEmployeeInterfaceDTO, TdPlanEmployeeInterface> {

}
