package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmPersonalIncomeTaxInterfaceDTO;
import com.sunten.hrms.swm.domain.SwmPersonalIncomeTaxInterface;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-01-14
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmPersonalIncomeTaxInterfaceMapper extends BaseMapper<SwmPersonalIncomeTaxInterfaceDTO, SwmPersonalIncomeTaxInterface> {

}
