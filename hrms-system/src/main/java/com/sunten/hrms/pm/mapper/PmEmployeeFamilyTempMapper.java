package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmEmployeeFamilyTempDTO;
import com.sunten.hrms.pm.domain.PmEmployeeFamilyTemp;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2020-08-25
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeeFamilyTempMapper extends BaseMapper<PmEmployeeFamilyTempDTO, PmEmployeeFamilyTemp> {

}
