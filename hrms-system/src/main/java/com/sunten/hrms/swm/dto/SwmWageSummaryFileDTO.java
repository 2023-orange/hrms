package com.sunten.hrms.swm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.math.BigDecimal;

/**
 * @author liangjw
 * @since 2020-12-25
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmWageSummaryFileDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 所得期间（格式：年.月）
    private String incomePeriod;

    // 工牌号
    private String workCard;

    // 姓名
    private String name;

    // 员工类别
    private String employeeCategory;

    // 包干区分（1包干，0非包干）
    private Boolean divisionContractFlag;

    // 部门
    private String department;

    // 科室
    private String administrativeOffice;

    // 班组
    private String team;

    // 岗位
    private String station;

    // 银行账号
    private String bankAccount;

    // 开户行名称
    private String bankName;

    // 岗位技能工资
    private BigDecimal postSkillSalary;

    // 目标绩效工资
    private BigDecimal targetPerformancePay;

    // 调配绩效工资（车间导入）
    private BigDecimal allocatePerformancePay;

    // 应发工资(固定)
    private BigDecimal wagesPayablePost;

    // 应发工资(浮动)
    private BigDecimal wagesPayableFloat;

    // 应发工资
    private BigDecimal wagesPayable;

    // 基本工资
    private BigDecimal overtimePay;

    // 实发工资（固定）
    private BigDecimal netPaymentPost;

    // 实发工资（浮动）
    private BigDecimal netPaymentFloat;

    // 扣除金额
    private BigDecimal deductionAmount;

    // 实发工资
    private BigDecimal netPayment;

    // 一孩补贴
    private BigDecimal oneChildSubsidy;

    // 安全累积奖
    private BigDecimal safetyAccumulationAward;

    // 工龄津贴
    private BigDecimal seniorityAllowance;

    // 高温补贴
    private BigDecimal highTemperatureSubsidy;

    // 交通津贴
    private BigDecimal transportationAllowance;

    // 岗位补贴
    private BigDecimal postSubsidy;

    // 补贴扣除
    private BigDecimal allowanceDeduction;

    // 扣除医药费
    private BigDecimal deductMedicalCosts;

    // 扣除_其它
    private BigDecimal deductOther;

    // 类别
    private String type;

    // 成本中心名称
    private String costCenter;

    // 成本中心号
    private String costCenterNum;

    // 生产系数
    private BigDecimal productionFactor;

    // 质量系数
    private BigDecimal qualityFactor;

    // 考核系数
    private BigDecimal assessmentCoefficient;

    // 服务部门
    private String serviceDepartment;

    // 扣除公积金（个人）
    private BigDecimal personalDeductAccumulationFund;

    // 扣除公积金（公司）
    private BigDecimal companyDeductAccumulationFund;

    // 扣除水电房
    private BigDecimal deductHydropowerHouse;

    // 扣除所得税
    private BigDecimal deductIncomeTax;

    // 扣除保险(个人)
    private BigDecimal personalDeductibles;

    // 扣除保险(公司)
    private BigDecimal companyDeductibles;

    private Long id;

    private String idNumber;

    // 工资条使用， 基本工资
    private BigDecimal subBasePay;
    // 工资条使用， 岗位技能工资
    private BigDecimal subPostSkillSalary;
    // 工资条使用， 包干工资
    private BigDecimal subLumpSumWage;
    // 扣除其它税前
    private BigDecimal deductOtherBefore;


}
