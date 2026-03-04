package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalTime;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.domain.FndDeptSnapshot;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 部门考勤制度关系表
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcDeptAttendance extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 部门id
     */
    private Long deptId;

    private FndDept fndDept;

    /**
     * 考勤制度ID
     */
    private Long regimeId;

    private AcRegime acRegime;

    /**
     * 工作日历id
     */
    private Long calendarHeaderId;

    private AcCalendarHeader acCalendarHeader;
    /**
     * 一班时间开始
     */
    private LocalTime firstTimeFrom;

    /**
     * 一班时间结束
     */
    private LocalTime firstTimeTo;

    /**
     * 一班是否跨日
     */
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
     * 生效日期(填写加1日)
     */
    @NotNull
    private LocalDate takeEffectDate;

    /**
     * 失效日期(只看日期不看时分秒)
     */
    private LocalDate invalidDate;

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
     *  @Date: 2020/10/16 9:15
     *  @Description: 部门快照
     */
    private FndDeptSnapshot fndDeptSnapshot;


}
