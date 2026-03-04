package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndSuperQueryTempDTO;
import com.sunten.hrms.fnd.domain.FndSuperQueryTemp;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-08-19
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndSuperQueryTempMapper extends BaseMapper<FndSuperQueryTempDTO, FndSuperQueryTemp> {

}
