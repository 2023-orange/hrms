package com.sunten.hrms.re.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用人需求表
 * </p>
 *
 * @author liangjw
 * @since 2021-04-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class ReDemand extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 需求编号
     */
    @NotBlank
    private String demandCode;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 科室名称
     */
    private String officeName;

    private String team;

    /**
     * 部门科室id
     */
    @NotNull
    private Long deptId;

    /**
     * 招聘类别
     */
    @NotBlank
    private String demandClass;

    /**
     * 往期人数
     */
    private String pastQuantity;

    /**
     * 当前需求情况
     */
    private String currentStatus;

    /**
     * 招聘来源
     */
    @NotBlank
    private String recruitmentSource;

    /**
     * 申报理由
     */
    private String demandReason;

    /**
     * 发起人id
     */
    private Long demandBy;

    /**
     * 撤销标识
     */
    @NotNull
    private Boolean repealFlag;

    /**
     * 撤销时间
     */
    private LocalDateTime repealDate;

    /**
     * 撤销意见
     */
    private String repealReason;

    /**
     * 审批意见
     */
    private String approvalResult;

    /**
     * OA单号
     */
    private String oaOrder;

    /**
     * 审批日期
     */
    private LocalDateTime approvalDate;

    /**
     * 需求状态
     */
    private String demandStatus;

    /**
     * 需求状态描述
     */
    private String statusDescribes;

    /**
     * 招聘过程记录
     */
    private String reRemake;

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;

    /**
     * 申报类别
     */
    private String demandReasonType;

    /**
     * 用人需求状态（用于控制页面，notCommit、inApproval、notPass、complete）
     */
    @NotBlank
    private String realDemandStatus;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private List<ReDemandJob> demandJobList;

    private String demandByStr;

    // 当前审批节点
    private String currentNode;

    // 当前审批人
    private String currentPerson;

    // 用于判定审批结束后，需求提交人是否拥有编辑按钮的控制标记
    private Boolean haveSubNeedFlag;

    // 用于判定dept是否变化
    private Boolean deptChangeFlag;

    private Long number;

    // 部门同期当年人数
    private Integer deptFirstCount;
    // 部门去年人数
    private Integer deptSecondCount;
    // 部门前年人数
    private Integer deptThirdCount;
    // 招聘过程记录跟踪
    private List<ReDemandTracking> reDemandTrackings;
    // 是否全部岗位需求通过, 通过true
    private Boolean allPassFlag;
    // 用于区分临时工是否生效
    private Boolean tempWorkFlag;
    // 临时需求开始日期
    private LocalDate tempBeginDate;
    // 临时需求结束日期
    private LocalDate tempEndDate;
    // 控制审批后能否继续编辑的标志
    private Boolean afterCompleteEditFlag;
    // 部门当月人数
    private Integer deptCurrentCount;
    // 剩余需求人数
    private Integer residualDemand;

    private String descriptionRemarks;
    //同意用人数
    private Integer agreeNum;
    // 同意用人数记录跟踪
    private List<ReDemandAgreeNum> reDemandAgreeNumList;
}
