package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdPlanInterfaceDTO;
import com.sunten.hrms.td.domain.TdPlanInterface;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-05-23
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdPlanInterfaceMapper extends BaseMapper<TdPlanInterfaceDTO, TdPlanInterface> {

}
