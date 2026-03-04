package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdPlanEmployeeDTO;
import com.sunten.hrms.td.domain.TdPlanEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-05-25
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdPlanEmployeeMapper extends BaseMapper<TdPlanEmployeeDTO, TdPlanEmployee> {

}
