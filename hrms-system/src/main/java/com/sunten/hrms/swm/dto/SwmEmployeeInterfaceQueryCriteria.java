package com.sunten.hrms.swm.dto;

import com.sunten.hrms.swm.domain.SwmEmployeeInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author liangjw
 * @since 2021-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SwmEmployeeInterfaceQueryCriteria extends SwmSalaryInterfaceQueryCriteria implements Serializable {
    private List<SwmEmployeeInterface> swmEmployeeInterfaceList;
}
