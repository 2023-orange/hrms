package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.domain.PmEmployeeVocational;
import com.sunten.hrms.pm.domain.PmEmployeeVocationalTemp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * @atuthor xukai
 * @date 2020/8/25 11:14
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeeVocationalCheckMapper extends BaseMapper<PmEmployeeVocationalTemp, PmEmployeeVocational> {

    @Mappings({
            @Mapping(source = "vocationalTemp.employeeVocational.id",target = "id"),
            @Mapping(source = "id",target = "vocationalTemp.id"),
            @Mapping(source = "checkFlag",target = "checkFlag")
    })
    PmEmployeeVocational toEntity(PmEmployeeVocationalTemp vocationalTemp);
}
