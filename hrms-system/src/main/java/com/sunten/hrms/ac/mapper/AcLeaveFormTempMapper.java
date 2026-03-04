package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.AcLeaveFormTempDTO;
import com.sunten.hrms.ac.domain.AcLeaveFormTemp;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2020-10-20
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcLeaveFormTempMapper extends BaseMapper<AcLeaveFormTempDTO, AcLeaveFormTemp> {

}
