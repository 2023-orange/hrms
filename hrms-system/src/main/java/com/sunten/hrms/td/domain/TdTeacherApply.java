package com.sunten.hrms.td.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.tool.domain.ToolLocalStorage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 讲师身份（申请）表
 * </p>
 *
 * @author xukai
 * @since 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdTeacherApply extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 讲师
     */
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
    private BigDecimal otherExperienceCompose;

    /**
     * 我司经验年限
     */
    private BigDecimal myExperienceCompose;

    /**
     * 曾授课主题
     */
    private String everExperience;

    /**
     * 擅长领域
     */
    private String beGoodAt;

    /**
     * 部门意见
     */
    private String deptOpinion;

    /**
     * 部门审核人
     */
    private String deptBy;

    /**
     * 评审组意见：符合、不符合
     */
    private String judgeOpinion;

    /**
     * 经办人
     */
    private String judgeBy;

    /**
     * 提交标识，0为保存，1为提交审批
     */
    @NotNull
    private Boolean submitFlag;

    /**
     * 讲师等级
     */
    private String level;

    /**
     * 讲师分数
     */
    private BigDecimal score;

    /**
     * 线下认证附件
     */
    private ToolLocalStorage storage;

    /**
     * 部门审核日期
     */
    private LocalDateTime deptOpinionDate;

    /**
     * 评审组审核日期
     */
    private LocalDateTime judgeOpinionDate;

    // 认证结果，0认证不通过，1认证通过
    private Boolean attribute3;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;
    /**
     * 认证标识，0未认证，1已认证
     */
    private Boolean attribute1;

    private String attribute4;

    private String attribute5;

    private LocalDateTime attribute2;

    /**
     * 最终审批结果
     */
    private String approvalResult;

    /**
     * OA单号
     */
    private String oaOrder;

    /**
     * 审批结束日期
     */
    private LocalDateTime approvalDate;

    /**
     * 当前审批节点
     */
    private String approvalNode;

    /**
     * 审批人
     */
    private String approvalEmployee;

}
