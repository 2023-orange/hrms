package com.sunten.hrms.kpi.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeDTO;
import com.sunten.hrms.kpi.domain.KpiDepartmentTree;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zhoujy
 * @since 2023-11-27
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KpiDepartmentTreeMapper extends BaseMapper<KpiDepartmentTreeDTO, KpiDepartmentTree> {

}
