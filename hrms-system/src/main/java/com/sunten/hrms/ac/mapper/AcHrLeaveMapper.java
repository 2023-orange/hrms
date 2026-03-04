package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.ac.domain.AcLeaveApplication;
import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.AcLeaveApplicationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zouyp
 * @since 2023-05-29
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcHrLeaveMapper extends BaseMapper<AcLeaveApplicationDTO, AcLeaveApplication> {

}
