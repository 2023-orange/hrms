package com.sunten.hrms.re.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;

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
 * 工作经历表
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class ReWorkhistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 招骋ID
     */
    @NotNull
    private Long reId;
    @TableField(exist = false)
    private ReRecruitment recruitment;

    /**
     * 单位
     */
    @NotBlank
    private String company;

    /**
     * 职务
     */
    @NotBlank
    private String post;

    /**
     * 开始时间
     */
    @NotNull
    private LocalDate startTime;

    /**
     * 结束时间
     */
    @NotNull
    private LocalDate endTime;

    /**
     * 月薪
     */
    private String salaryOld;

    /**
     * 离职原因
     */
    private String reasonsLeaving;

    /**
     * 证明人
     */
    private String witness;

    /**
     * 联系电话
     */
    private String tele;

    /**
     * 备注
     */
    private String remarks;

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
     * 有效标记默认值
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private String workTimeNew;
}
