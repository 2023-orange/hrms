package com.sunten.hrms.re.dto;

    import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author xukai
 * @since 2021-04-25
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ReTrialApprovalDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 招聘人员信息
    private ReRecruitmentDTO trialEmployee;

    // 部门面试人
    private String interviewBy;

    // 当前需求情况
    private String currentStatus;

    // 总评
    private String appraise;

    // 面试详细评价
    private String interviewContent;

    // 试用期工资
    private Double salary;

    // 试用时间（月）
    private Integer trialTime;

    // 体检类型（普通、特殊）
    private String medicalClass;

    // 撤销标识
    private Boolean repealFlag;

    // 撤销时间
    private LocalDateTime repealDate;

    // 撤销意见
    private String repealReason;

    // 最终审批意见
    private String approvalResult;

    // OA审批单号
    private String oaOrder;

    // 审批结束日期
    private LocalDateTime approvalDate;

    // 试用情况（试用、未试用）
    private String trialStatus;

    // 未试用情况描述
    private String failDescribes;

    // 有效标识
    private Boolean enabledFlag;

    //提交审批标识
    private Boolean submitFlag;

    // 候选人入职时间（试用时填写）
    private LocalDate trialEntryTime;

    // 未试用原因：个人原因、体检不合格、编制原因、其他原因
    private String trialFailReason;

    private Long id;

    // 审批人姓名
    private String approvalEmployee;
    //当前审批节点
    private String approvalNode;

    // 用人需求信息
    private ReDemandDTO demand;

    // 用人需求岗位信息
    private ReDemandJobDTO demandJob;

    private Integer surplusQuantity; // 当前需求剩余人数
}
