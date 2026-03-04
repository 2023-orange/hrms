package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmLeaveApprovalDTO;
import com.sunten.hrms.pm.domain.PmLeaveApproval;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-05-07
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmLeaveApprovalMapper extends BaseMapper<PmLeaveApprovalDTO, PmLeaveApproval> {

}
