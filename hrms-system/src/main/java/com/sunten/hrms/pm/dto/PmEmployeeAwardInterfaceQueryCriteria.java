package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author xk
 * @since 2021-09-23
 */
@Data
public class PmEmployeeAwardInterfaceQueryCriteria implements Serializable {
    private Long groupId;
    private String dataStatus;
}
