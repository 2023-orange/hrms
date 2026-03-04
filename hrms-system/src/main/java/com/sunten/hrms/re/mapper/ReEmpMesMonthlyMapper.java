package com.sunten.hrms.re.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.re.dto.ReEmpMesMonthlyDTO;
import com.sunten.hrms.re.domain.ReEmpMesMonthly;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2022-01-07
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReEmpMesMonthlyMapper extends BaseMapper<ReEmpMesMonthlyDTO, ReEmpMesMonthly> {

}
