package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.domain.SwmFloatingWage;
import com.sunten.hrms.swm.dto.SwmFloatingWageDTO;
import com.sunten.hrms.swm.dto.SwmFloatingWageSpecialDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmFloatingWageSpecialMapper extends BaseMapper<SwmFloatingWageSpecialDTO, SwmFloatingWage> {
}
