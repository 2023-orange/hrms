package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.AcCalendarLineDTO;
import com.sunten.hrms.ac.domain.AcCalendarLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author ljw
 * @since 2020-09-17
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcCalendarLineMapper extends BaseMapper<AcCalendarLineDTO, AcCalendarLine> {

}
