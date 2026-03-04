package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.AcAbnormalAttendanceRecordDTO;
import com.sunten.hrms.ac.domain.AcAbnormalAttendanceRecord;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2020-10-15
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcAbnormalAttendanceRecordMapper extends BaseMapper<AcAbnormalAttendanceRecordDTO, AcAbnormalAttendanceRecord> {

}
