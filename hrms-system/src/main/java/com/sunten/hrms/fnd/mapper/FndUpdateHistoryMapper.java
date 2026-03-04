package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndUpdateHistoryDTO;
import com.sunten.hrms.fnd.domain.FndUpdateHistory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2020-07-24
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndUpdateHistoryMapper extends BaseMapper<FndUpdateHistoryDTO, FndUpdateHistory> {

}
