package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.domain.PmEmployeeTitle;
import com.sunten.hrms.pm.domain.PmEmployeeTitleTemp;
import com.sunten.hrms.pm.dto.PmEmployeeTitleTempDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2020-08-04
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeeTitleCheckMapper extends BaseMapper<PmEmployeeTitleTemp, PmEmployeeTitle> {
    @Mappings({
            @Mapping(source = "employeeTitleTemp.employeeTitle.id",target = "id"),
            @Mapping(source = "id",target = "titleTemp.id"),
            @Mapping(source = "checkFlag",target = "checkFlag")
    })
    PmEmployeeTitle toEntity(PmEmployeeTitleTemp employeeTitleTemp);
}
