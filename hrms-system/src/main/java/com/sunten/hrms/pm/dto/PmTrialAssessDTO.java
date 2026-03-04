package com.sunten.hrms.pm.dto;

    import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.swm.dto.SwmEmployeeDTO;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author xukai
 * @since 2021-05-07
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmTrialAssessDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 人事档案ID
//    private Long employeeId;
    private PmEmployeeDTO pmEmployee;
    // 考核起始日期
    private LocalDate beginDate;

    // 考核结束日期
    private LocalDate endDate;

    // 出勤情况评分
    private Integer attendanceGrade;

    // 工作态度评分
    private Integer workAttitudeGrade;

    // 工作效率评分
    private Integer workIfficiencyGrade;

    // 工作能力及项目技能评分
    private Integer capacityAndSkill;

    // 服从性及合作性评分
    private Integer complianceAndCooperation;

    // 总评分
    private Integer totalGrade;

    // 总评语
    private String lastEvaluate;

    // 试用意见（录用、不录用）
    private String trailResult;

    // 拟定岗位
    private String jobName;

    // 职类
    private String category;

    // 岗位类别
    private String jobClass;

    // 建议包干工资
    private BigDecimal lumpSumWage;

    // 建议岗位技能工资
    private BigDecimal baseSalary;

    // 建议目标绩效工资
    private BigDecimal performanceSalary;

    // OA审批单号
    private String oaOrder;

    // 审批结束日期
    private LocalDateTime approvalDate;

    // 有效标识
    private Boolean enabledFlag;

    // 提交审批标识
    private Boolean submitFlag;

    // 当前审批节点
    private String approvalNode;

    // 当前审批人
    private String approvalEmployee;
    // 最终审批结果
    private String approvalResult;

    private Long id;

    private Boolean sellJob;
    private BigDecimal approvalBaseSalary;
    private BigDecimal approvalPerformanceSalary;
    private BigDecimal approvalLumpSumWage;

    private Boolean adminFlag; // 管理岗标识
    private String salaryProposal; // 薪资建议
    private BigDecimal salaryChangeRange; // 薪资调整幅度
    private BigDecimal salaryIdea; // 建议薪资

    private String currentNode; // 当前进度
    private Long leaderBy; // 当前处理领导的人事id
    private Boolean eligibleFile; // 文件合格标记
    private Integer fileDegree; // 重新提交文件的次数
    private LocalDateTime lastFileTime; // 最后一次提交文件时间

    private String jobType; // 职种
    private Long entryId; // 入职情况id
    // 薪酬员工信息
    private SwmEmployeeDTO swmEmployee;
}
