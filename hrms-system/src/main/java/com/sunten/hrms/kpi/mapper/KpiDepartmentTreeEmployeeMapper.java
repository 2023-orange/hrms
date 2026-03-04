package com.sunten.hrms.kpi.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeEmployeeDTO;
import com.sunten.hrms.kpi.domain.KpiDepartmentTreeEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zhoujy
 * @since 2023-11-27
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KpiDepartmentTreeEmployeeMapper extends BaseMapper<KpiDepartmentTreeEmployeeDTO, KpiDepartmentTreeEmployee> {

}
