package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.EmployeeDZBDTO;
import com.sunten.hrms.pm.domain.EmployeeDZB;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-09-09
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeDZBMapper extends BaseMapper<EmployeeDZBDTO, EmployeeDZB> {

}
