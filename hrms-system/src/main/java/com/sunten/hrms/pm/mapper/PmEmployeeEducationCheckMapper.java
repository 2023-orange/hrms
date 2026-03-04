package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.domain.PmEmployeeEducation;
import com.sunten.hrms.pm.domain.PmEmployeeEducationTemp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * @atuthor xukai
 * @date 2020/8/25 11:11
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeeEducationCheckMapper extends BaseMapper<PmEmployeeEducationTemp, PmEmployeeEducation> {

    @Mappings({
            @Mapping(source = "pmEmployeeEducationTemp.employeeEducation.id",target = "id"),
            @Mapping(source = "id",target = "educationTemp.id"),
            @Mapping(source = "checkFlag",target = "checkFlag")
    })
    PmEmployeeEducation toEntity(PmEmployeeEducationTemp pmEmployeeEducationTemp);
}
