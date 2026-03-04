package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryDTO;
import com.sunten.hrms.pm.domain.PmEmployeeWorkhistory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2020-08-04
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeeWorkhistoryMapper extends BaseMapper<PmEmployeeWorkhistoryDTO, PmEmployeeWorkhistory> {

}
