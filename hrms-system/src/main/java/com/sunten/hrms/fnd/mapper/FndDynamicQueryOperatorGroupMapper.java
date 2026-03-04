package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndDynamicQueryOperatorGroupDTO;
import com.sunten.hrms.fnd.domain.FndDynamicQueryOperatorGroup;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2022-07-26
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndDynamicQueryOperatorGroupMapper extends BaseMapper<FndDynamicQueryOperatorGroupDTO, FndDynamicQueryOperatorGroup> {

}
