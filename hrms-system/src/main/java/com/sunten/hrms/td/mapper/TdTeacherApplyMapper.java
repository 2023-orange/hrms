package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdTeacherApplyDTO;
import com.sunten.hrms.td.domain.TdTeacherApply;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-06-15
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdTeacherApplyMapper extends BaseMapper<TdTeacherApplyDTO, TdTeacherApply> {

}
