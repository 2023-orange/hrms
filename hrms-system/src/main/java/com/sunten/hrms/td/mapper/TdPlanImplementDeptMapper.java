package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdPlanImplementDeptDTO;
import com.sunten.hrms.td.domain.TdPlanImplementDept;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-06-21
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdPlanImplementDeptMapper extends BaseMapper<TdPlanImplementDeptDTO, TdPlanImplementDept> {

}
