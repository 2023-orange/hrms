package com.sunten.hrms.tool.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.tool.dto.ToolLocalStorageDTO;
import com.sunten.hrms.tool.domain.ToolLocalStorage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2019-12-25
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ToolLocalStorageMapper extends BaseMapper<ToolLocalStorageDTO, ToolLocalStorage> {

}
