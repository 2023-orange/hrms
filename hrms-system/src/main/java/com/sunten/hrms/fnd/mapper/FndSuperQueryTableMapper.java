package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndSuperQueryTableDTO;
import com.sunten.hrms.fnd.domain.FndSuperQueryTable;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2022-08-12
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndSuperQueryTableMapper extends BaseMapper<FndSuperQueryTableDTO, FndSuperQueryTable> {

}
