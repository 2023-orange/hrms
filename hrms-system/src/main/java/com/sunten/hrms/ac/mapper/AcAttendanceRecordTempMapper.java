package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.AcAttendanceRecordTempDTO;
import com.sunten.hrms.ac.domain.AcAttendanceRecordTemp;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2020-10-15
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcAttendanceRecordTempMapper extends BaseMapper<AcAttendanceRecordTempDTO, AcAttendanceRecordTemp> {

}
