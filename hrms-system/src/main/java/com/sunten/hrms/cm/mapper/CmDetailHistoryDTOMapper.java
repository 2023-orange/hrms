package com.sunten.hrms.cm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.cm.domain.CmDetail;
import com.sunten.hrms.cm.domain.CmDetailHistory;
import com.sunten.hrms.cm.dto.CmDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zhoujy
 * @since 2023-02-23
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CmDetailHistoryDTOMapper extends BaseMapper<CmDetailDTO, CmDetailHistory> {
}
