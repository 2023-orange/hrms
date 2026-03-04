package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdJobGradingDTO;
import com.sunten.hrms.td.domain.TdJobGrading;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2022-03-22
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdJobGradingMapper extends BaseMapper<TdJobGradingDTO, TdJobGrading> {

}
