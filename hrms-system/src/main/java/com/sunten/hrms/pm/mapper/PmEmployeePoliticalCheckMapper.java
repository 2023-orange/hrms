package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.domain.PmEmployeePolitical;
import com.sunten.hrms.pm.domain.PmEmployeePoliticalTemp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-11-24
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeePoliticalCheckMapper extends BaseMapper<PmEmployeePoliticalTemp, PmEmployeePolitical> {
    @Mappings({
            @Mapping(source = "politicalTemp.employeePolitical.id",target = "id"),
            @Mapping(source = "id",target = "politicalTemp.id"),
            @Mapping(source = "checkFlag",target = "checkFlag")
    })
    PmEmployeePolitical toEntity(PmEmployeePoliticalTemp politicalTemp);
}
