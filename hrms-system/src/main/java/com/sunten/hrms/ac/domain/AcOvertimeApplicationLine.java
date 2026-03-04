package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author zouyp
 * @since 2023-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcOvertimeApplicationLine extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Integer id;

    /**
     * OA申请单号
     */
    private String oaOrder;

    /**
     * 加班人姓名
     */
    private String nickName;

    /**
     * 加班人工号
     */
    private String userName;

    /**
     * 岗位
     */
    private String position;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 休息时间
     */
    private Float totalRestTime;

    /**
     * 加班时数
     */
    private Float hours;

    /**
     * 当月已加班时数
     */
    private Float monthHours;

    /**
     * 复核开始时间
     */
    @TableField("review_startTime")
    private LocalDateTime reviewStarttime;

    /**
     * 复核结束时间
     */
    @TableField("review_endTime")
    private LocalDateTime reviewEndtime;

    /**
     * 复核休息时间
     */
    private Float reviewTotalRestTime;

    /**
     * 复核加班时数
     */
    private Float reviewHours;

    /**
     * 复核人姓名
     */
    private String reviewerNickName;

    /**
     * 复核人工号
     */
    private String reviewerUserName;

    /**
     * 复核时间
     */
    private LocalDateTime reviewTime;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 有效标记
     */
    private Boolean enabledFlag;

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
     * 弹性域4
     */
    private String attribute4;

    /**
     * 弹性域5
     */
    private String attribute5;


}
