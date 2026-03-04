package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * oa加班请假统计接口表
 * </p>
 *
 * @author liangjw
 * @since 2020-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcOvertimeLeaveInterface extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    private LocalDate date;

    /**
     * 工牌号
     */
    private String workCard;

    private String name;

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

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


    private Long groupId;

    // 操作码
    private String  operationCode;

    // 错误信息
    private String errorMsg;

    // 数据状态
    private  String dataStatus;

    // 日期字符串
    private String dateStr;
    // 计划生育假
    private BigDecimal familyPlanningLeave;
    // 公假
    private BigDecimal publicHoliday;

    private BigDecimal totalOverTime;

    private Boolean preCheckFlag;

    private Integer order;



}
