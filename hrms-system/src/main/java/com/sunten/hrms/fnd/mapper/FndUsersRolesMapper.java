package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndUsersRolesDTO;
import com.sunten.hrms.fnd.domain.FndUsersRoles;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2019-12-19
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndUsersRolesMapper extends BaseMapper<FndUsersRolesDTO, FndUsersRoles> {

}
