package com.sunten.hrms.re.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.re.dto.ReDemandJobDescribesDTO;
import com.sunten.hrms.re.domain.ReDemandJobDescribes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-04-23
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReDemandJobDescribesMapper extends BaseMapper<ReDemandJobDescribesDTO, ReDemandJobDescribes> {

}
