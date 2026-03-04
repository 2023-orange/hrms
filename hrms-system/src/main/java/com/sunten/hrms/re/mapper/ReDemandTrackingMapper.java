package com.sunten.hrms.re.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.re.dto.ReDemandTrackingDTO;
import com.sunten.hrms.re.domain.ReDemandTracking;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2022-01-18
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReDemandTrackingMapper extends BaseMapper<ReDemandTrackingDTO, ReDemandTracking> {

}
