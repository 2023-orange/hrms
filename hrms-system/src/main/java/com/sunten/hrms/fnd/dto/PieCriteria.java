package com.sunten.hrms.fnd.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author batan
 * @since 2019-12-19
 */
@Data
public class PieCriteria implements Serializable {
    private Long deptId;

    private String typeCheckBox;
}
