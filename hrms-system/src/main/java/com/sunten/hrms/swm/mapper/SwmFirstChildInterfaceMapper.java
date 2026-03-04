package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmFirstChildInterfaceDTO;
import com.sunten.hrms.swm.domain.SwmFirstChildInterface;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-08-10
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmFirstChildInterfaceMapper extends BaseMapper<SwmFirstChildInterfaceDTO, SwmFirstChildInterface> {

}
