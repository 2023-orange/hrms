package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdJobAuthenticationInterfaceDTO;
import com.sunten.hrms.td.domain.TdJobAuthenticationInterface;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-10-11
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdJobAuthenticationInterfaceMapper extends BaseMapper<TdJobAuthenticationInterfaceDTO, TdJobAuthenticationInterface> {

}
