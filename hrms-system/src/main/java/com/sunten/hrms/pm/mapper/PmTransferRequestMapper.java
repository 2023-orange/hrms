package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmTransferRequestDTO;
import com.sunten.hrms.pm.domain.PmTransferRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-05-24
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmTransferRequestMapper extends BaseMapper<PmTransferRequestDTO, PmTransferRequest> {

}
