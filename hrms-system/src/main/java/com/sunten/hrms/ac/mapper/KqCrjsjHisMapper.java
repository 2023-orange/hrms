package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.KqCrjsjHisDTO;
import com.sunten.hrms.ac.domain.KqCrjsjHis;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2020-10-19
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KqCrjsjHisMapper extends BaseMapper<KqCrjsjHisDTO, KqCrjsjHis> {

}
