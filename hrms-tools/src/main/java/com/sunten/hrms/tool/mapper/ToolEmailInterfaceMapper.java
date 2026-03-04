package com.sunten.hrms.tool.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.dto.ToolEmailInterfaceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2020-10-30
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ToolEmailInterfaceMapper extends BaseMapper<ToolEmailInterfaceDTO, ToolEmailInterface> {

}
