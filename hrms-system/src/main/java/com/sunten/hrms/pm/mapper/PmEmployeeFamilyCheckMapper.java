package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.domain.PmEmployeeFamily;
import com.sunten.hrms.pm.domain.PmEmployeeFamilyTemp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * @atuthor xukai
 * @date 2020/8/25 9:59
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeeFamilyCheckMapper extends BaseMapper<PmEmployeeFamilyTemp,PmEmployeeFamily> {

    @Mappings({
            @Mapping(source = "familyTemp.employeeFamily.id",target = "id"),
            @Mapping(source = "id",target = "familyTemp.id"),
            @Mapping(source = "checkFlag",target = "checkFlag")
    })
    PmEmployeeFamily toEntity(PmEmployeeFamilyTemp familyTemp);
}
