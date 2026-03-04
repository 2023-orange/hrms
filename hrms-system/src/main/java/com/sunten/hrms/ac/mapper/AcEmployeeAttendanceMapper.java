package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.AcEmployeeAttendanceDTO;
import com.sunten.hrms.ac.domain.AcEmployeeAttendance;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author ljw
 * @since 2020-09-17
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcEmployeeAttendanceMapper extends BaseMapper<AcEmployeeAttendanceDTO, AcEmployeeAttendance> {

}
