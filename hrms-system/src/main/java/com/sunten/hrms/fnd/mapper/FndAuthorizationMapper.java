package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndAuthorizationDTO;
import com.sunten.hrms.fnd.domain.FndAuthorization;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-01-29
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndAuthorizationMapper extends BaseMapper<FndAuthorizationDTO, FndAuthorization> {

}
