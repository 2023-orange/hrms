package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.ac.domain.AcVacate;
import com.sunten.hrms.ac.dto.AcVacateDTO;
import com.sunten.hrms.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @atuthor xukai
 * @date 2020/10/15 11:26
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcVacateMapper extends BaseMapper<AcVacateDTO, AcVacate> {

}
