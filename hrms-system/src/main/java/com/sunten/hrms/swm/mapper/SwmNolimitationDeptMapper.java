package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmNolimitationDeptDTO;
import com.sunten.hrms.swm.domain.SwmNolimitationDept;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zhoujy
 * @since 2023-11-21
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmNolimitationDeptMapper extends BaseMapper<SwmNolimitationDeptDTO, SwmNolimitationDept> {

}
