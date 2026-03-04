package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdCoursewareDTO;
import com.sunten.hrms.td.domain.TdCourseware;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-06-18
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdCoursewareMapper extends BaseMapper<TdCoursewareDTO, TdCourseware> {

}
