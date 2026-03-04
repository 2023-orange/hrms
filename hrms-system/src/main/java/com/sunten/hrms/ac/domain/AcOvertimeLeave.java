package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * oa加班请假统计
 * </p>
 *
 * @author liangjw
 * @since 2020-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcOvertimeLeave extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    private LocalDate date;

    /**
     * 员工id
     */
    private Long employeeId;

    /**
     * 工作日加班（周一到周五加班）
     */
    private BigDecimal workingDayOvertime;

    /**
     * 休息日加班（周六日加班）
     */
    private BigDecimal restDayOvertime;

    /**
     * 法定节假日加班时数
     */
    private BigDecimal holidayOvertime;

    /**
     * 调休
     */
    private BigDecimal offHours;

    /**
     * 年假
     */
    private BigDecimal annualLeave;

    /**
     * 事假
     */
    private BigDecimal compassionateLeave;

    /**
     * 病假
     */
    private BigDecimal sickLeave;

    /**
     * 婚假
     */
    private BigDecimal marriageHoliday;

    /**
     * 产假
     */
    private BigDecimal maternityLeave;

    /**
     * 陪产假
     */
    private BigDecimal paternityLeave;

    /**
     * 丧假
     */
    private BigDecimal bereavementLeave;

    /**
     * 工伤假
     */
    private BigDecimal workRelatedInjuryLeave;


    // 应上班工时（时）
    private BigDecimal workingHours;

    // 应上班工时（天）
    private BigDecimal workingHoursDay;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private PmEmployee employee;


    private Boolean enabledFlag;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    // 实际上班工时
    private BigDecimal totalWorkingHours;

    private Boolean checkFlag = false;

    private String team;

    private String dept;

    private BigDecimal familyPlanningLeave;

    private BigDecimal publicHoliday;

    private BigDecimal totalOverTime;
    // 入职时间
    private LocalDate entryTime;
    // 离职时间
    private LocalDate quitTime;

}
