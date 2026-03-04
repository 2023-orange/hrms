package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.domain.FndUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2019-12-19
 */

@Mapper(componentModel = "spring",uses = {FndRoleMapper.class, FndDeptMapper.class, FndJobMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndUserMapper extends BaseMapper<FndUserDTO, FndUser> {

    @Mapping(source = "user.userAvatar.realName",target = "avatar")
    FndUserDTO toDto(FndUser user);
}