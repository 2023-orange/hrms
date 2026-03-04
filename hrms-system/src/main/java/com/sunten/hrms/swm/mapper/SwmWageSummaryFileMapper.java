package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmWageSummaryFileDTO;
import com.sunten.hrms.swm.domain.SwmWageSummaryFile;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2020-12-25
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmWageSummaryFileMapper extends BaseMapper<SwmWageSummaryFileDTO, SwmWageSummaryFile> {

}
