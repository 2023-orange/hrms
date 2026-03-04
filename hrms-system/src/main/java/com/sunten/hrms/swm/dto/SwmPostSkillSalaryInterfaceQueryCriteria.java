package com.sunten.hrms.swm.dto;

import com.sunten.hrms.swm.domain.SwmPostSkillSalaryInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SwmPostSkillSalaryInterfaceQueryCriteria extends SwmSalaryInterfaceQueryCriteria implements Serializable {
    private List<SwmPostSkillSalaryInterface> swmPostSkillSalaryInterfaces;
}
