package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.domain.PmEmployeeHobby;
import com.sunten.hrms.pm.domain.PmEmployeeHobbyTemp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * @atuthor xukai
 * @date 2020/8/25 11:14
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeeHobbyCheckMapper extends BaseMapper<PmEmployeeHobbyTemp, PmEmployeeHobby> {

    @Mappings({
            @Mapping(source = "hobbyTemp.employeeHobby.id",target = "id"),
            @Mapping(source = "id",target = "hobbyTemp.id"),
            @Mapping(source = "checkFlag",target = "checkFlag")
    })
    PmEmployeeHobby toEntity(PmEmployeeHobbyTemp hobbyTemp);
}
