package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.AcHrLeaveSubDTO;
import com.sunten.hrms.ac.domain.AcLeaveApplicationLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zouyp
 * @since 2023-05-30
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcHrLeaveSubMapper extends BaseMapper<AcHrLeaveSubDTO, AcLeaveApplicationLine> {

}
