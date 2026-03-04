package com.sunten.hrms.tool.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.tool.dto.ToolEmailServerDTO;
import com.sunten.hrms.tool.domain.ToolEmailServer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2020-11-02
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ToolEmailServerMapper extends BaseMapper<ToolEmailServerDTO, ToolEmailServer> {

}
