package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmEmpDeptDTO;
import com.sunten.hrms.swm.domain.SwmEmpDept;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-02-24
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmEmpDeptMapper extends BaseMapper<SwmEmpDeptDTO, SwmEmpDept> {

}
