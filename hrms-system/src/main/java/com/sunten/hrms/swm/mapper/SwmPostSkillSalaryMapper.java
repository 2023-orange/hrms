package com.sunten.hrms.swm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryDTO;
import com.sunten.hrms.swm.domain.SwmPostSkillSalary;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwmPostSkillSalaryMapper extends BaseMapper<SwmPostSkillSalaryDTO, SwmPostSkillSalary> {

}
