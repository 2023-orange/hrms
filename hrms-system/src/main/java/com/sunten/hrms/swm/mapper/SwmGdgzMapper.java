package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmGdgzDTO;
import com.sunten.hrms.swm.domain.SwmGdgz;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zhoujy
 * @since 2023-04-07
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmGdgzMapper extends BaseMapper<SwmGdgzDTO, SwmGdgz> {

}
