package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdCoursewareEmployeeDTO;
import com.sunten.hrms.td.domain.TdCoursewareEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2022-03-14
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdCoursewareEmployeeMapper extends BaseMapper<TdCoursewareEmployeeDTO, TdCoursewareEmployee> {

}
