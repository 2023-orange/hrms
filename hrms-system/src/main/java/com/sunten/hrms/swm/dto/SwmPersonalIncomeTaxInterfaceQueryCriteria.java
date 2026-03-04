package com.sunten.hrms.swm.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2021-01-14
 */
@Data
public class SwmPersonalIncomeTaxInterfaceQueryCriteria implements Serializable {
    private Long groupId;
    private String dataStatus;
}
