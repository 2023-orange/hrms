package com.sunten.hrms.ac.dto;

import com.sunten.hrms.base.BaseDTO;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author liangjw
 * @since 2020-10-26
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AcOvertimeLeaveDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 日期
    private LocalDate date;

    // 员工id
    private Long employeeId;

    // 工作日加班（周一到周五加班）
    private BigDecimal workingDayOvertime;

    // 休息日加班（周六日加班）
    private BigDecimal restDayOvertime;

    private BigDecimal holidayOvertime;

    // 调休
    private BigDecimal offHours;

    // 年假
    private BigDecimal annualLeave;

    // 事假
    private BigDecimal compassionateLeave;

    // 病假
    private BigDecimal sickLeave;

    // 婚假
    private BigDecimal marriageHoliday;

    // 产假
    private BigDecimal maternityLeave;

    // 陪产假
    private BigDecimal paternityLeave;

    // 丧假
    private BigDecimal bereavementLeave;

    // 工伤假
    private BigDecimal workRelatedInjuryLeave;

    // 工时
    private BigDecimal workingHours;

    private Long id;

    private PmEmployeeDTO employee;

    private Boolean enabledFlag;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    // 实际上班工时
    private BigDecimal totalWorkingHours;

    private Boolean checkFlag = false;


    // 应上班工时（天）
    private BigDecimal workingHoursDay;

    // 计划生育假
    private BigDecimal familyPlanningLeave;
    // 公假
    private BigDecimal publicHoliday;

    private BigDecimal totalOverTime;
    // 入职时间
    private LocalDate entryTime;
    // 离职时间
    private LocalDate quitTime;
}
