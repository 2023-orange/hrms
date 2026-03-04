package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndInterfaceOperationRecordDTO;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2020-10-26
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndInterfaceOperationRecordMapper extends BaseMapper<FndInterfaceOperationRecordDTO, FndInterfaceOperationRecord> {

}
