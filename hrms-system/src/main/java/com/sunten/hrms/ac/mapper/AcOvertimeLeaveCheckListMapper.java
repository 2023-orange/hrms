package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.ac.domain.AcOvertimeApplication;
import com.sunten.hrms.ac.domain.OvertimeLeaveCheckList;
import com.sunten.hrms.ac.dto.AcOvertimeApplicationDTO;
import com.sunten.hrms.ac.dto.OvertimeLeaveCheckListDTO;
import com.sunten.hrms.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zouyp
 * @since 2023-10-16
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcOvertimeLeaveCheckListMapper extends BaseMapper<OvertimeLeaveCheckListDTO, OvertimeLeaveCheckList> {

}
