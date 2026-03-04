package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmEmployeeMonthlyBakDTO;
import com.sunten.hrms.swm.domain.SwmEmployeeMonthlyBak;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-09-15
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmEmployeeMonthlyBakMapper extends BaseMapper<SwmEmployeeMonthlyBakDTO, SwmEmployeeMonthlyBak> {

}
