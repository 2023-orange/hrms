package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author batan
 * @since 2020-08-04
 */
@Data
public class PmEmployeeEducationTempQueryCriteria implements Serializable {
    private Long educationId;
    private Boolean enabled;

    private Long employeeId;
    private String checkFlag;//校核标记，D待校核，Y通过，N不通过
    private String instructionsFlag;//操作类型： A添加、 U修改
}
