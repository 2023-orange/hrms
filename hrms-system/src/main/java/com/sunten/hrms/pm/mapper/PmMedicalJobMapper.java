package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmMedicalJobDTO;
import com.sunten.hrms.pm.domain.PmMedicalJob;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-04-19
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmMedicalJobMapper extends BaseMapper<PmMedicalJobDTO, PmMedicalJob> {

}
