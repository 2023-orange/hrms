package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndRoleDTO;
import com.sunten.hrms.fnd.domain.FndRole;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2019-12-19
 */
@Mapper(componentModel = "spring", uses = {FndMenuMapper.class, FndDeptMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndRoleMapper extends BaseMapper<FndRoleDTO, FndRole> {

}
