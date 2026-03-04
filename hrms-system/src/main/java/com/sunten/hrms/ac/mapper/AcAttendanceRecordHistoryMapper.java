package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.AcAttendanceRecordHistoryDTO;
import com.sunten.hrms.ac.domain.AcAttendanceRecordHistory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

/**
 * @author liangjw
 * @since 2020-10-15
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcAttendanceRecordHistoryMapper extends BaseMapper<AcAttendanceRecordHistoryDTO, AcAttendanceRecordHistory> {
    Set<AcAttendanceRecordHistoryDTO> toDto(Set<AcAttendanceRecordHistory> entityList);
}
