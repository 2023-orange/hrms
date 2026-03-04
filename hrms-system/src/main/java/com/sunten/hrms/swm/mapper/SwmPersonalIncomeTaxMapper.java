package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmPersonalIncomeTaxDTO;
import com.sunten.hrms.swm.domain.SwmPersonalIncomeTax;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmPersonalIncomeTaxMapper extends BaseMapper<SwmPersonalIncomeTaxDTO, SwmPersonalIncomeTax> {

}
