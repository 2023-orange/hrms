package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmEmployeePostotherTempDTO;
import com.sunten.hrms.pm.domain.PmEmployeePostotherTemp;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-11-24
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeePostotherTempMapper extends BaseMapper<PmEmployeePostotherTempDTO, PmEmployeePostotherTemp> {

}
