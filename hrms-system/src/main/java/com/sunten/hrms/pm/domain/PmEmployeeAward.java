package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;

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
 * 奖罚情况表
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeAward extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 员工id
     */
//    @NotNull
//    private Long employeeId;
    @TableField(exist = false)
    private PmEmployee employee;

    /**
     * 奖励：Reward；扣罚：Fine
     */
//    @NotBlank
    private String type;

    /**
     * 奖罚名称
     */
//    @NotBlank
    private String awardName;

    /**
     * 奖罚处理开始时间
     */
//    @NotNull
    private LocalDateTime awardStarTime;

    /**
     * 奖罚处理结束时间
     */
//    @NotNull
    private LocalDateTime awardEndTime;

    /**
     * 奖罚单位
     */
//    @NotBlank
    private String awardCompany;

    /**
     * 奖罚内容
     */
    private String awardContent;

    /**
     * 奖罚结果
     */
    private String awardResult;

    /**
     * 奖罚金额
     */
    private Double awardMoney;

    /**
     * 是否有备查资料
     */
//    @NotNull
    private Boolean referenceBackupFlag;

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


}
