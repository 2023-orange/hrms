package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.domain.PmEmployeePostother;
import com.sunten.hrms.pm.domain.PmEmployeePostotherTemp;
import com.sunten.hrms.pm.dto.PmEmployeePostotherTempDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-11-24
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeePostotherCheckMapper extends BaseMapper<PmEmployeePostotherTemp, PmEmployeePostother> {
    @Mappings({
            @Mapping(source = "postotherTemp.employeePostother.id",target = "id"),
            @Mapping(source = "id",target = "postotherTemp.id"),
            @Mapping(source = "checkFlag",target = "checkFlag")
    })
    PmEmployeePostother toEntity(PmEmployeePostotherTemp postotherTemp);
}
