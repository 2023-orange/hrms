package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.AcAbnormalClockRecordDTO;
import com.sunten.hrms.ac.domain.AcAbnormalClockRecord;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2020-10-15
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcAbnormalClockRecordMapper extends BaseMapper<AcAbnormalClockRecordDTO, AcAbnormalClockRecord> {

}
