package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndDeptSnapshotDTO;
import com.sunten.hrms.fnd.domain.FndDeptSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2020-10-15
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndDeptSnapshotMapper extends BaseMapper<FndDeptSnapshotDTO, FndDeptSnapshot> {

}
