package com.sunten.hrms.ac.domain;

import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author Zouyp
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcClockRecordRest extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long employeeId;
    /**
     * 工号
     */
    private String workCard;

    /**
     * 打卡日期
     */
    @NotNull
    private LocalDate date;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String gender;

    /**
     * 部门
     */
    private String extDeptName;
    /**
     * 科室
     */
    private String extDepartmentName;
    /**
     * 班组
     */
    private String extTeamName;

    /**
     * 连续打卡天数
     */
    private Integer consecutiveDays;

    /**
     * 加班时间
     */
    private Double overtime;
    /**
     * clock_times 打卡时间
     */
    private String clockTimes;
    /**
     * 向后找最后一个连续打卡日期
     */
    private Date latestClockDate;
    /**
     * 考勤机说明
     */
    private String attendanceMachine;
}
