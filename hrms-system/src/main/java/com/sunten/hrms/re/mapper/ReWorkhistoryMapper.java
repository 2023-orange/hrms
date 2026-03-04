package com.sunten.hrms.re.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.re.dto.ReWorkhistoryDTO;
import com.sunten.hrms.re.domain.ReWorkhistory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2020-08-05
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReWorkhistoryMapper extends BaseMapper<ReWorkhistoryDTO, ReWorkhistory> {

}
