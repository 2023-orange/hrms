package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmEmployeeInfoInterfaceDTO;
import com.sunten.hrms.swm.domain.SwmEmployeeInfoInterface;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zhoujy
 * @since 2023-03-23
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmEmployeeInfoInterfaceMapper extends BaseMapper<SwmEmployeeInfoInterfaceDTO, SwmEmployeeInfoInterface> {

}
