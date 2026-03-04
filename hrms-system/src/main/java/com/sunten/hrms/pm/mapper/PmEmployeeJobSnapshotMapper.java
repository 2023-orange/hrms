package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmEmployeeJobSnapshotDTO;
import com.sunten.hrms.pm.domain.PmEmployeeJobSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2020-10-15
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeeJobSnapshotMapper extends BaseMapper<PmEmployeeJobSnapshotDTO, PmEmployeeJobSnapshot> {


}
