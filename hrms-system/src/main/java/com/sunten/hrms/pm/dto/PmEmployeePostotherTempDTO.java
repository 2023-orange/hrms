package com.sunten.hrms.pm.dto;

import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xukai
 * @since 2021-11-24
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmEmployeePostotherTempDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private PmEmployeePostotherDTO employeePostother;

    private PmEmployeeDTO employee;

    // 单位
    private String company;

    // 职务
    private String post;

    // 备注
    private String remarks;

    // 操作标记
    private String instructionsFlag;

    // 校核标记
    private String checkFlag;

    private Boolean enabledFlag;

    private Long id;


}
