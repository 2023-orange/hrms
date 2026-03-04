package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.domain.PmEmployeeSocialrelations;
import com.sunten.hrms.pm.domain.PmEmployeeSocialrelationsTemp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-11-24
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeeSocialrelationsCheckMapper extends BaseMapper<PmEmployeeSocialrelationsTemp, PmEmployeeSocialrelations> {
    @Mappings({
            @Mapping(source = "socialrelationsTemp.employeeSocialrelations.id",target = "id"),
            @Mapping(source = "id",target = "socialrelationsTemp.id"),
            @Mapping(source = "checkFlag",target = "checkFlag")
    })
    PmEmployeeSocialrelations toEntity(PmEmployeeSocialrelationsTemp socialrelationsTemp);
}
