package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndJobDTO;
import com.sunten.hrms.fnd.domain.FndJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2019-12-19
 */
@Mapper(componentModel = "spring",uses = {FndDeptMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndJobMapper extends BaseMapper<FndJobDTO, FndJob> {

    @Mapping(source = "deptSuperiorName", target = "deptSuperiorName")
    FndJobDTO toDto(FndJob job, String deptSuperiorName);
}