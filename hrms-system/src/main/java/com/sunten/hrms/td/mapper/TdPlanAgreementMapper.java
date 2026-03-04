package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdPlanAgreementDTO;
import com.sunten.hrms.td.domain.TdPlanAgreement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-06-18
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdPlanAgreementMapper extends BaseMapper<TdPlanAgreementDTO, TdPlanAgreement> {

}
