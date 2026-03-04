package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyDTO;
import com.sunten.hrms.swm.domain.SwmConsolationMoney;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-08-04
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmConsolationMoneyMapper extends BaseMapper<SwmConsolationMoneyDTO, SwmConsolationMoney> {

}
