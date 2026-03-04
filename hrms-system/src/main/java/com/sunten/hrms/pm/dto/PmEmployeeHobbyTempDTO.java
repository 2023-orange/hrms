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
public class PmEmployeeHobbyTempDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;


    private PmEmployeeHobbyDTO employeeHobby;

    private PmEmployeeDTO employee;

    // 技能爱好
    private String hobby;

    // 级别
    private String levelMyself;

    // 认证等级
    private String levelMechanism;

    // 备注
    private String remarks;

    // 操作标记
    private String instructionsFlag;

    // 校核标记
    private String checkFlag;

    private Long id;

    private Boolean enabledFlag;


}
