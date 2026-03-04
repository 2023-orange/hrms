package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.PmLeaveApplicationDTO;
import com.sunten.hrms.ac.domain.PmLeaveApplication;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zouyp
 * @since 2023-06-12
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmLeaveApplicationMapper extends BaseMapper<PmLeaveApplicationDTO, PmLeaveApplication> {

}
