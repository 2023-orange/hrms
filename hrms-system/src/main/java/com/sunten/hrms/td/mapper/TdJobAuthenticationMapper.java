package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdJobAuthenticationDTO;
import com.sunten.hrms.td.domain.TdJobAuthentication;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-06-22
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdJobAuthenticationMapper extends BaseMapper<TdJobAuthenticationDTO, TdJobAuthentication> {

}
