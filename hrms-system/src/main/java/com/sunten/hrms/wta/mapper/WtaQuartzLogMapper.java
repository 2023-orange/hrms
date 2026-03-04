package com.sunten.hrms.wta.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.wta.dto.WtaQuartzLogDTO;
import com.sunten.hrms.wta.domain.WtaQuartzLog;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2019-12-23
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WtaQuartzLogMapper extends BaseMapper<WtaQuartzLogDTO, WtaQuartzLog> {

}
