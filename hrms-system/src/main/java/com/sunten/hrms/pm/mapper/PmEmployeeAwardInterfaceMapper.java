package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmEmployeeAwardInterfaceDTO;
import com.sunten.hrms.pm.domain.PmEmployeeAwardInterface;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xk
 * @since 2021-09-23
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeeAwardInterfaceMapper extends BaseMapper<PmEmployeeAwardInterfaceDTO, PmEmployeeAwardInterface> {

}
