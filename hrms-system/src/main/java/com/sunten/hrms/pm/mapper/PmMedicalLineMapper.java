package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmMedicalLineDTO;
import com.sunten.hrms.pm.domain.PmMedicalLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-04-07
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmMedicalLineMapper extends BaseMapper<PmMedicalLineDTO, PmMedicalLine> {

}
