package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.ac.domain.AcOvertime;
import com.sunten.hrms.ac.dto.AcOvertimeDTO;
import com.sunten.hrms.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @atuthor xukai
 * @date 2020/10/16 15:56
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcOvertimeMapper extends BaseMapper<AcOvertimeDTO, AcOvertime> {

}
