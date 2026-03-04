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
public class PmEmployeeSocialrelationsTempDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private PmEmployeeSocialrelationsDTO employeeSocialrelations;

    private PmEmployeeDTO employee;

    // 姓名
    private String name;

    // 关系
    private String relationship;

    // 单位
    private String company;

    // 职务
    private String post;

    // 电话
    private String tele;

    // 是否在厂工作
    private Boolean inFactoryFlag;

    // 操作标记
    private String instructionsFlag;

    // 校核标记
    private String checkFlag;

    private Long id;

    private Boolean enabledFlag;


}
