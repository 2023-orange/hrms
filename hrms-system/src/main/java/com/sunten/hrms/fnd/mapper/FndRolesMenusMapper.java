package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndRolesMenusDTO;
import com.sunten.hrms.fnd.domain.FndRolesMenus;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2019-12-19
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndRolesMenusMapper extends BaseMapper<FndRolesMenusDTO, FndRolesMenus> {

}
