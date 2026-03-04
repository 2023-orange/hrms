package com.sunten.hrms.td.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2021-09-24
 */
@Data
public class TdPlanEmployeeInterfaceQueryCriteria implements Serializable {
    private Long groupId;

    private String dataStatus;

}
