package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.AcOvertimeApplicationLineDTO;
import com.sunten.hrms.ac.domain.AcOvertimeApplicationLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zouyp
 * @since 2023-10-16
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcOvertimeApplicationLineMapper extends BaseMapper<AcOvertimeApplicationLineDTO, AcOvertimeApplicationLine> {

}
