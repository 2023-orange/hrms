package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.mapper.FndDeptMapper;
import com.sunten.hrms.fnd.mapper.FndJobMapper;
import com.sunten.hrms.fnd.mapper.FndRoleMapper;
import com.sunten.hrms.pm.domain.PmMedicalTuboo;
import com.sunten.hrms.pm.dto.PmMedicalTubooDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zhoujy
 * @since 2022-11-30
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmMedicalTubooMapper extends BaseMapper<PmMedicalTubooDTO, PmMedicalTuboo> {

}
