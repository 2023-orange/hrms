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
 * @since 2021-05-24
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmTransferRequestDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 调动编号
    private String transferCode;

    // 人员id
//    private Long pmEmployeeId;
    private PmEmployeeDTO pmEmployee;
    // 调出岗薪资(永久调动用)
    private BigDecimal fromSalary;

    // 调入部门名称
    private String toDeptName;

    // 调入科室名称
    private String toOfficeName;


    // 调入班组名称
    private String toTeamName;

    // 调入部门id
    private Long toDeptId;

    // 调入岗位
    private String toJobName;

    /**
     * 认证岗位
     */
    private String certificationJob;

    /**
     * 是否需要上岗认证标识
     */
    private Boolean needAuthenticationFlag;


    // 调入岗位id
    private Long toJobId;

    // 调入岗实习工资(永久调动用)
    private BigDecimal internshipSalary;

    // 调入岗上岗工资(永久调动用)
    private BigDecimal formalSalary;

    // 调岗原因
    private String reason;

    // 本人意见
    private String selfIdea;

    // 本人意见填写日期
    private LocalDate ideaDate;

    // 最终审批结果
    private String approvalResult;

    // OA单号
    private String oaOrder;

    // 审批结束日期
    private LocalDateTime approvalDate;

    // 生效标记
    private Boolean enabledFlag;

    // 当前审批节点
    private String approvalNode;

    // 审批人
    private String approvalEmployee;

    // 提交标识，0为保存，1为提交审批
    private Boolean submitFlag;

    // 对应员工需求审批表编号(永久调动用)
    private String demandCode;


    /**
     * 对应员工需求审批表的子表ID(永久调动用)
     */
    private Long demandJobId;

    // 调岗其它原因描述
    private String reasonOtherDescribes;

    // 借调起始日期
    private LocalDate beginDate;

    // 借调结束日期
    private LocalDate endDate;

    // 借调生效日期
    private LocalDate infaceBeginDate;

    // 调出部门名称
    private String fromDeptName;

    // 调出科室名称
    private String fromOfficeName;
    // 调出班组名称
    private String fromTeamName;

    // 调出部门id
    private Long fromDeptId;

    // 调出岗位
    private String fromJobName;

    // 调出岗位id
    private Long fromJobId;

    // long: 永久调动，short：借调
    private String transferType;

    // 体检类型，安委会审批时填写
    private String medicalType;

    // 体检结果
    private String medicalResult;

    // 体检结果回填时间
    private LocalDate medicalDate;

    /**
     * 调动日期，由“招聘专员1”最后一个OA节点返写到数据表。
     */
    private LocalDate transferDate;

    private String attribute3;

    private String attribute4;

    private String attribute1;

    private String attribute5;

    private String swmChangeFlag;

    private String teacherContractFlag;

    private Long attribute2;

    private Long id;

    // 人员流转记录
    private PmTransferCommentDTO pmTransferComment;

    // 上岗资格认证标记
    private Boolean authenticationFlag;
    // 是否调动标记
    private Boolean transferFlag;
    private String salaryChange; // 薪资变化
    // 薪酬员工信息
    private SwmEmployeeDTO swmEmployee;

    // 调出岗位薪资组成
    private String fromSalaryStr;


    // 上岗包干工资
    private BigDecimal dutyLumpSumWage;

    // 上岗基本工资
    private BigDecimal dutyBasePay;

    // 上岗岗位技能工资
    private BigDecimal dutyPostSkillSalary;

    // 上岗目标绩效工资
    private BigDecimal dutyTargetPerformancePay ;

    // 上岗服务部门
    private String dutyServiceDepartment;

    // 上岗班长津贴
    private BigDecimal dutySquadLeaderAllowance ;

    // 上岗岗位补贴
    private BigDecimal dutyPostSubsidy;

    // 上岗工龄津贴
    private BigDecimal dutySeniorityAllowance;

    // 上岗交通津贴
    private BigDecimal dutyTransportationAllowance;



    // 实习包干工资
    private BigDecimal praLumpSumWage;

    // 实习基本工资
    private BigDecimal praBasePay;

    // 实习岗位技能工资
    private BigDecimal praPostSkillSalary;

    // 实习目标绩效工资
    private BigDecimal praTargetPerformancePay;

    // 实习服务部门
    private String praServiceDepartment;


    // 实习班长津贴
    private BigDecimal praSquadLeaderAllowance;

    // 实习岗位补贴
    private BigDecimal praPostSubsidy;

    // 实习工龄津贴
    private BigDecimal praSeniorityAllowance;

    // 实习交通津贴
    private BigDecimal praTransportationAllowance;

    //是否已经提交申请表
    private Boolean submitFormFlag;

}
