package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.ac.domain.AcClockRecordRest;
import com.sunten.hrms.ac.dto.AcClockRecordRestDTO;
import com.sunten.hrms.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * @author Zouyp
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcClockRecordRestMapper extends BaseMapper<AcClockRecordRestDTO, AcClockRecordRest> {

}
