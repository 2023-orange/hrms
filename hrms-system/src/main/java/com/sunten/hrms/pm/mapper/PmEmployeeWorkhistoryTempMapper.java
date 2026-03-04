package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryTempDTO;
import com.sunten.hrms.pm.domain.PmEmployeeWorkhistoryTemp;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-11-24
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeeWorkhistoryTempMapper extends BaseMapper<PmEmployeeWorkhistoryTempDTO, PmEmployeeWorkhistoryTemp> {

}
