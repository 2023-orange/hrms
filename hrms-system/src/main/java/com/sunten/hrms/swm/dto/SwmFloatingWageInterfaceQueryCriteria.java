package com.sunten.hrms.swm.dto;

import com.sunten.hrms.swm.domain.SwmFloatingWageInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SwmFloatingWageInterfaceQueryCriteria extends SwmSalaryInterfaceQueryCriteria implements Serializable {

    private List<SwmFloatingWageInterface> swmFloatingWageInterfaceList;

}
