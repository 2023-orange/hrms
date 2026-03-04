package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndRolesDeptsDTO;
import com.sunten.hrms.fnd.domain.FndRolesDepts;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2019-12-19
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndRolesDeptsMapper extends BaseMapper<FndRolesDeptsDTO, FndRolesDepts> {

}
