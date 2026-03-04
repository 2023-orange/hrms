package com.sunten.hrms.cm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.cm.dto.CmDetailHistoryDTO;
import com.sunten.hrms.cm.domain.CmDetailHistory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zhoujy
 * @since 2023-02-23
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CmDetailHistoryMapper extends BaseMapper<CmDetailHistoryDTO, CmDetailHistory> {

}
