package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmSalatax2006DTO;
import com.sunten.hrms.swm.domain.SwmSalatax2006;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zhoujy
 * @since 2023-04-11
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmSalatax2006Mapper extends BaseMapper<SwmSalatax2006DTO, SwmSalatax2006> {

}
