package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndDynamicQueryGroupDetailDTO;
import com.sunten.hrms.fnd.domain.FndDynamicQueryGroupDetail;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2022-07-29
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndDynamicQueryGroupDetailMapper extends BaseMapper<FndDynamicQueryGroupDetailDTO, FndDynamicQueryGroupDetail> {

}
