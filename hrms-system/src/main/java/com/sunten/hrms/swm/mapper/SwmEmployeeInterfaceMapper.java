package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmEmployeeInterfaceDTO;
import com.sunten.hrms.swm.domain.SwmEmployeeInterface;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-09-13
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmEmployeeInterfaceMapper extends BaseMapper<SwmEmployeeInterfaceDTO, SwmEmployeeInterface> {

}
