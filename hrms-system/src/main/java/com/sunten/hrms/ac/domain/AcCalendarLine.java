package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 日历详细表
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcCalendarLine extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 日历主表id
     */
    @NotNull
    private Long calendarHeaderId;

    /**
     * 日期
     */
    @NotNull
    private LocalDate nowDate;

    /**
     * 星期
     */
    @NotBlank
    private String week;

    /**
     * 是否休息日
     */
    @NotNull
    private Boolean dayOffFlag;

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
     *  @Date: 2020/9/25 13:56
     *  @Description: 分批时用的排序
     */
    @TableField(exist = false)
    private Integer order;

    private List<AcEmployeeAttendance> acEmployeeAttendances;

}
