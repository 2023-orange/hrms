package com.sunten.hrms.fnd.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author batan
 * @since 2019-12-06
*/
@Data
@ToString
public class FndDeptSmallDTO implements Serializable {

    private Long id;

    private String deptName;

    private String extDeptName;

    private String extDepartmentName;

    private String extTeamName;
}
