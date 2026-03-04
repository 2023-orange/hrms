package com.sunten.hrms.re.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.re.dto.ReDemandJobDTO;
import com.sunten.hrms.re.domain.ReDemandJob;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-04-23
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReDemandJobMapper extends BaseMapper<ReDemandJobDTO, ReDemandJob> {

}
