package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmMedicalLineRelevanceDTO;
import com.sunten.hrms.pm.domain.PmMedicalLineRelevance;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-04-20
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmMedicalLineRelevanceMapper extends BaseMapper<PmMedicalLineRelevanceDTO, PmMedicalLineRelevance> {

}
