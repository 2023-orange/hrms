package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndServerInformationDTO;
import com.sunten.hrms.fnd.domain.FndServerInformation;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2024-06-06
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndServerInformationMapper extends BaseMapper<FndServerInformationDTO, FndServerInformation> {

}
