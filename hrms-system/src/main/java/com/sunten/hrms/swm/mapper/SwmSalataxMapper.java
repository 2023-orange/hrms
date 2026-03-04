package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmSalataxDTO;
import com.sunten.hrms.swm.domain.SwmSalatax;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zhoujy
 * @since 2023-04-10
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmSalataxMapper extends BaseMapper<SwmSalataxDTO, SwmSalatax> {

}
