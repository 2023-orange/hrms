package com.sunten.hrms.td.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 培训讲师列表
 * </p>
 *
 * @author xukai
 * @since 2021-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdTeacher extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 讲师ID
     */
//    @NotNull
//    private Long employeeId;
    private PmEmployee pmEmployee;
    /**
     * 课程主题
     */
    @NotBlank
    private String title;

    /**
     * 课程内容
     */
    @NotBlank
    private String content;

    /**
     * 授课经验（年）: 他司+我司
     */
    @NotNull
    private BigDecimal teachExperience;

    /**
     * 他司经验年限
     */
    @NotNull
    private BigDecimal otherExperienceCompose;

    /**
     * 我司经验年限
     */
    @NotNull
    private BigDecimal myExperienceCompose;

    /**
     * 曾授课课题
     */
    private String everExperience;

    /**
     * 擅长领域
     */
    private String beGoodAt;

    /**
     * 讲师等级
     */
    private String level;

    /**
     * 讲师分数
     */
    private BigDecimal score;

    /**
     * 认证时间
     */
    private LocalDateTime passDate;

    /**
     * 来源：线上申请、线下录入
     */
    private String attribute1;

    /**
     * 记录OA单号，如果是线上申请产生的
     */
    private String attribute2;

    private String attribute5;

    private String attribute4;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private String attribute3;


}
