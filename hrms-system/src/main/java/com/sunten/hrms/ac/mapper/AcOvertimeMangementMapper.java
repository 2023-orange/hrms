package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.AcOvertimeMangementDTO;
import com.sunten.hrms.ac.domain.AcOvertimeMangement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zouyp
 * @since 2023-10-16
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcOvertimeMangementMapper extends BaseMapper<AcOvertimeMangementDTO, AcOvertimeMangement> {

}
