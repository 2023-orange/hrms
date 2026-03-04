package com.sunten.hrms.ac.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liangjw
 * @since 2020-10-26
 */
@Data
public class AcOvertimeLeaveInterfaceQueryCriteria implements Serializable {
    private Long groupId;

    private String dataStatus;

}
