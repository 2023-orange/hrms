package com.sunten.hrms.pm.dto;

import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author xukai
 * @since 2021-11-24
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmEmployeePoliticalTempDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private PmEmployeePoliticalDTO employeePolitical;

    private PmEmployeeDTO employee;

    // 政治面貌
    private String political;

    // 加入时间
    private LocalDate joiningTime;

    // 性质
    private String nature;

    // 转正时间
    private LocalDate formalTime;

    // 职务
    private String post;

    // 开始时间
    private LocalDate startTime;

    // 结束时间
    private LocalDate endTime;

    // 操作标记
    private String instructionsFlag;

    // 校核标记
    private String checkFlag;

    private Boolean enabledFlag;

    private Long id;

    private String attribute1;
}
