package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndAuthorizationDtsDTO;
import com.sunten.hrms.fnd.domain.FndAuthorizationDts;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-02-02
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndAuthorizationDtsMapper extends BaseMapper<FndAuthorizationDtsDTO, FndAuthorizationDts> {

}
