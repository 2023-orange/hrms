package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalTime;
import java.util.List;

import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 排班员工考勤制度关系表
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcEmployeeAttendance extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 员工人事档案id
     */
    @NotNull
    private Long employeeId;

    private PmEmployee employee;

    /**
     * 排班日期
     */
    @NotNull
    private LocalDate regimeaDate;

    /**
     * 一班时间开始
     */
    @NotNull
    private LocalTime firstTimeFrom;

    /**
     * 一班时间结束
     */
    @NotNull
    private LocalTime firstTimeTo;

    /**
     * 一班是否跨日
     */
    @NotNull
    private Boolean extend1TimeFlag;

    /**
     * 二班时间开始
     */
    private LocalTime secondTimeFrom;

    /**
     * 二班时间结束
     */
    private LocalTime secondTimeTo;

    /**
     * 二班是否跨日
     */
    private Boolean extend2TimeFlag;

    /**
     * 三班时间开始
     */
    private LocalTime thirdTimeFrom;

    /**
     * 三班时间结束
     */
    private LocalTime thirdTimeTo;

    /**
     * 三班是否跨日
     */
    private Boolean extend3TimeFlag;

    /**
     * 弹性域1
     */
    private String attribute1;

    /**
     * 弹性域2
     */
    private String attribute2;

    /**
     * 弹性域3
     */
    private String attribute3;

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;
    /**
     *  @author：liangjw
     *  @Date: 2020/9/27 16:19
     *  @Description: 是否休息日，默认0
     */
    private Boolean restDayFlag;
}
