package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmEmployeeEducationDTO;
import com.sunten.hrms.pm.domain.PmEmployeeEducation;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2020-08-04
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeeEducationMapper extends BaseMapper<PmEmployeeEducationDTO, PmEmployeeEducation> {

}
