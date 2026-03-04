package com.sunten.hrms.td.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
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
 * 计划实施申请
 * </p>
 *
 * @author liangjw
 * @since 2021-05-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdPlanImplement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 培训计划id
     */
    @NotNull
    private Long planId;

    /**
     * 起始培训时间
     */
    @NotNull
    private LocalDate beginDate;

    /**
     * 培训结束时间
     */
    @NotNull
    private LocalDate endDate;

    /**
     * 培训时长
     */
    @NotNull
    private Float trainingTimeQuantity;

    /**
     * 培训地址
     */
    @NotBlank
    private String trainingAddress;

    /**
     * 考核方式
     */
    @NotBlank
    private String checkMethod;

    /**
     * 培训费用
     */
    @NotNull
    private BigDecimal trainingMoney;

    /**
     * 申请人的empId
     */
    @NotNull
    private Long requestBy;

    /**
     * 申请日期
     */
    private LocalDate requestDate;

    /**
     * 当前审批节点
     */
    private String currentNode;

    /**
     * 当前审批人
     */
    private String currentPerson;

    /**
     * OA单号
     */
    private String oaOrder;

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;

    /**
     * 申请状态
     */
    @NotBlank
    private String approvalStatus;

    /**
     * 外部讲师
     */
    private String outTeacher;

    /**
     * 外部参训人员
     */
    private String outEmp;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


    private TdPlan plan;

    // 在提交控制标记
    private Boolean reCommitFlag;

    private String paymentDes; // 培训花费明细

    private String overallAttendanceDescription; // 整体培训情况描述

    private Integer absenceTotal; // 缺席人数

    private Integer practicalParticipationTotal; // 实际参训人数


}
