package com.sunten.hrms.re.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.re.dto.ReTrialApprovalDTO;
import com.sunten.hrms.re.domain.ReTrialApproval;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-04-25
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReTrialApprovalMapper extends BaseMapper<ReTrialApprovalDTO, ReTrialApproval> {

}
