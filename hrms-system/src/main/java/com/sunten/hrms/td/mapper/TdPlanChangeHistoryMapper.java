package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdPlanChangeHistoryDTO;
import com.sunten.hrms.td.domain.TdPlanChangeHistory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-06-16
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdPlanChangeHistoryMapper extends BaseMapper<TdPlanChangeHistoryDTO, TdPlanChangeHistory> {

}
