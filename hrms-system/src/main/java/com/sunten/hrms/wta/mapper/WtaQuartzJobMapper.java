package com.sunten.hrms.wta.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.wta.dto.WtaQuartzJobDTO;
import com.sunten.hrms.wta.domain.WtaQuartzJob;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2019-12-23
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WtaQuartzJobMapper extends BaseMapper<WtaQuartzJobDTO, WtaQuartzJob> {

}
