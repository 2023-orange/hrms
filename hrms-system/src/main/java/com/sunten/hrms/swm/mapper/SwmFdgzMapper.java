package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmFdgzDTO;
import com.sunten.hrms.swm.domain.SwmFdgz;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zhoujy
 * @since 2023-04-10
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmFdgzMapper extends BaseMapper<SwmFdgzDTO, SwmFdgz> {

}
