package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyInterfaceDTO;
import com.sunten.hrms.swm.domain.SwmConsolationMoneyInterface;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-08-05
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmConsolationMoneyInterfaceMapper extends BaseMapper<SwmConsolationMoneyInterfaceDTO, SwmConsolationMoneyInterface> {

}
