package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.AcEmpDeptsDTO;
import com.sunten.hrms.ac.domain.AcEmpDepts;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2020-12-09
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcEmpDeptsMapper extends BaseMapper<AcEmpDeptsDTO, AcEmpDepts> {

}
