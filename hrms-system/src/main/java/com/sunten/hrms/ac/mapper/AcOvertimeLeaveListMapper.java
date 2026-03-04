package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.ac.domain.AcOvertimeBackUp;
import com.sunten.hrms.ac.domain.OvertimeLeaveCheckList;
import com.sunten.hrms.ac.dto.AcOvertimeBackUpDTO;
import com.sunten.hrms.ac.dto.OvertimeLeaveCheckListDTO;
import com.sunten.hrms.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zouyp
 * @since 2023-11-21
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcOvertimeLeaveListMapper extends BaseMapper<OvertimeLeaveCheckListDTO, OvertimeLeaveCheckList> {

}
