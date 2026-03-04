package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.domain.PmEmployeeWorkhistory;
import com.sunten.hrms.pm.domain.PmEmployeeWorkhistoryTemp;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryTempDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-11-24
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeeWorkhistoryCheckMapper extends BaseMapper<PmEmployeeWorkhistoryTemp, PmEmployeeWorkhistory> {
    @Mappings({
            @Mapping(source = "workhistoryTemp.employeeWorkhistory.id",target = "id"),
            @Mapping(source = "id",target = "workhistoryTemp.id"),
            @Mapping(source = "checkFlag",target = "checkFlag")
    })
    PmEmployeeWorkhistory toEntity(PmEmployeeWorkhistoryTemp workhistoryTemp);
}
