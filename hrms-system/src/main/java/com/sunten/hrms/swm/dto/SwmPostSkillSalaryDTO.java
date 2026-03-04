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
 * @since 2020-11-24
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmPostSkillSalaryDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 所得期间（格式：年.月）
    private String incomePeriod;

    // 工牌号
    private String workCard;

    // 员工姓名
    private String employeeName;

    // 员工类别
    private String employeeCategory;

    // 包干区分（1包干，0非包干）
    private Boolean divisionContractFlag;

    // 科室
    private String administrativeOffice;

    // 部门
    private String department;

    // 银行账号
    private String bankAccount;

    // 开户行名称
    private String bankName;

    // 岗位技能工资
    private BigDecimal postSkillSalary;

    // 工龄津贴
    private BigDecimal seniorityAllowance;

    // 一孩补贴
    private BigDecimal oneChildSubsidy;

    // 高温补贴
    private BigDecimal highTemperatureSubsidy;

    // 交通津贴
    private BigDecimal transportationAllowance;

    // 岗位补贴
    private BigDecimal postSubsidy;

    // 工作日加班工资
    private BigDecimal overtimePay;

    // 法定节假日加班工资
    private BigDecimal holidayOvertimePay;

    // 休息日加班工资
    private BigDecimal restOvertimePay;

    // 补贴扣除
    private BigDecimal allowanceDeduction;

    // 应发工资
    private BigDecimal wagesPayable;

    // 扣除医药费
    private BigDecimal deductMedicalCosts;

    // 扣除水电房
    private BigDecimal deductHydropowerHouse;

    // 扣除所得税（此项需要导入）
    private BigDecimal deductIncomeTax;

    // 扣除保险(个人)
    private BigDecimal personalDeductibles;

    // 扣除公积金（个人）
    private BigDecimal personalDeductAccumulationFund;

    // 扣除_其它
    private BigDecimal deductOther;

    // 扣除保险(公司)
    private BigDecimal companyDeductibles;

    // 扣除公积金（公司）
    private BigDecimal companyDeductAccumulationFund;

    // 成本中心名称
    private String costCenter;

    // 成本中心号
    private String costCenterNum;

    // 服务部门
    private String serviceDepartment;

    // 弹性域
    private BigDecimal attribute1;

    // 弹性域
    private BigDecimal attribute2;

    // 弹性域
    private BigDecimal attribute3;

    // 弹性域
    private BigDecimal attribute4;

    // 弹性域
    private BigDecimal attribute5;

    // 实发工资
    private BigDecimal netPayment;

    // 是否冻结
    private Boolean frozenFlag;

    // 安全累积奖
    private BigDecimal safetyAccumulationAward;

    private Long seId;

    private Boolean enabledFlag;

    private Boolean grantFlag;


    private String updateByStr;

    // 班组
    private String team;

    // 岗位
    private String station;

    private BigDecimal overtimePayTime;

    private BigDecimal holidayOvertimePayTime;

    private BigDecimal restOvertimePayTime;

    private Boolean specialImportFlag;


}
