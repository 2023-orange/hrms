package com.sunten.hrms.swm.dto;

import com.sunten.hrms.swm.domain.SwmEmployeeInfoInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhoujy
 * @since 2023-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SwmEmployeeInfoInterfaceQueryCriteria extends SwmSalaryInterfaceQueryCriteria implements Serializable {
    private List<SwmEmployeeInfoInterface> swmEmployeeInfoInterface;
}
