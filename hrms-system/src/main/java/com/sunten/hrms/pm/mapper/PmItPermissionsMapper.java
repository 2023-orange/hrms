package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmItPermissionsDTO;
import com.sunten.hrms.pm.domain.PmItPermissions;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-05-10
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmItPermissionsMapper extends BaseMapper<PmItPermissionsDTO, PmItPermissions> {

}
