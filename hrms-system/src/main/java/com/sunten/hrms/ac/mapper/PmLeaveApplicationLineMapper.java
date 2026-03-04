package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.PmLeaveApplicationLineDTO;
import com.sunten.hrms.ac.domain.PmLeaveApplicationLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zouyp
 * @since 2023-06-12
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmLeaveApplicationLineMapper extends BaseMapper<PmLeaveApplicationLineDTO, PmLeaveApplicationLine> {

}
