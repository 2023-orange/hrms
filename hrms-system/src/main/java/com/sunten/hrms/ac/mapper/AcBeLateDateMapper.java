package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.AcBeLateDateDTO;
import com.sunten.hrms.ac.domain.AcBeLateDate;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-07-08
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcBeLateDateMapper extends BaseMapper<AcBeLateDateDTO, AcBeLateDate> {

}
