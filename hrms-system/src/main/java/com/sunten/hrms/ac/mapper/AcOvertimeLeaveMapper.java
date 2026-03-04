package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.ac.domain.AcOvertimeLeave;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveDTO;
import com.sunten.hrms.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2020-10-26
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcOvertimeLeaveMapper extends BaseMapper<AcOvertimeLeaveDTO, AcOvertimeLeave> {

    AcOvertimeLeaveDTO toDto(AcOvertimeLeave overtimeLeave);
}
