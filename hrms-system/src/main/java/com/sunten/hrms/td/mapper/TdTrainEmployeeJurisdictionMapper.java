package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdTrainEmployeeJurisdictionDTO;
import com.sunten.hrms.td.domain.TdTrainEmployeeJurisdiction;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-06-23
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdTrainEmployeeJurisdictionMapper extends BaseMapper<TdTrainEmployeeJurisdictionDTO, TdTrainEmployeeJurisdiction> {

}
