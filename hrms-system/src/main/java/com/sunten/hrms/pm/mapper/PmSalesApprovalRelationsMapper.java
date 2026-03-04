package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmSalesApprovalRelationsDTO;
import com.sunten.hrms.pm.domain.PmSalesApprovalRelations;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2022-02-17
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmSalesApprovalRelationsMapper extends BaseMapper<PmSalesApprovalRelationsDTO, PmSalesApprovalRelations> {

}
