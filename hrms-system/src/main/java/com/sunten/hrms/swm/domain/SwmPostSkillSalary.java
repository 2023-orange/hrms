package com.sunten.hrms.swm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 岗位技能工资表
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmPostSkillSalary extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 所得期间（格式：年.月）
     */
    @NotBlank
    private String incomePeriod;

    /**
     * 工牌号
     */
    @NotBlank
    private String workCard;

    /**
     * 员工姓名
     */
    @NotBlank
    private String employeeName;

    /**
     * 员工类别
     */
    @NotBlank
    private String employeeCategory;

    /**
     * 包干区分（1包干，0非包干）
     */
    @NotNull
    private Boolean divisionContractFlag;

    /**
     * 科室
     */
    private String administrativeOffice;

    /**
     * 部门
     */
    @NotBlank
    private String department;

    /**
     * 银行账号
     */
    @NotBlank
    private String bankAccount;

    /**
     * 开户行名称
     */
    private String bankName;

    /**
     * 岗位技能工资
     */
    @NotNull
    private BigDecimal postSkillSalary;

    /**
     * 工龄津贴
     */
    private BigDecimal seniorityAllowance;

    /**
     * 一孩补贴
     */
    private BigDecimal oneChildSubsidy;

    /**
     * 高温补贴
     */
    private BigDecimal highTemperatureSubsidy;

    /**
     * 交通津贴
     */
    @NotNull
    private BigDecimal transportationAllowance;

    /**
     * 岗位补贴
     */
    private BigDecimal postSubsidy;

    /**
     * 工作日加班工资
     */
    private BigDecimal overtimePay;

    /**
     * 法定节假日加班工资
     */
    private BigDecimal holidayOvertimePay;

    /**
     * 休息日加班工资
     */
    private BigDecimal restOvertimePay;

    /**
     * 补贴扣除
     */
    @NotNull
    private BigDecimal allowanceDeduction;

    /**
     * 应发工资
     */
    private BigDecimal wagesPayable;

    /**
     * 扣除医药费
     */
    @NotNull
    private BigDecimal deductMedicalCosts;

    /**
     * 扣除水电房
     */
    @NotNull
    private BigDecimal deductHydropowerHouse;

    /**
     * 扣除所得税（此项需要导入）
     */
    private BigDecimal deductIncomeTax;

    /**
     * 扣除保险(个人)
     */
    @NotNull
    private BigDecimal personalDeductibles;

    /**
     * 扣除公积金（个人）
     */
    @NotNull
    private BigDecimal personalDeductAccumulationFund;

    /**
     * 扣除_其它
     */
    @NotNull
    private BigDecimal deductOther;

    /**
     * 扣除保险(公司)
     */
    private BigDecimal companyDeductibles;

    /**
     * 扣除公积金（公司）
     */
    @NotNull
    private BigDecimal companyDeductAccumulationFund;

    /**
     * 成本中心名称
     */
    @NotBlank
    private String costCenter;

    /**
     * 成本中心号
     */
    @NotBlank
    private String costCenterNum;

    /**
     * 服务部门
     */
    private String serviceDepartment;

    /**
     * 弹性域
     */
    private BigDecimal attribute1;

    /**
     * 弹性域
     */
    private BigDecimal attribute2;

    /**
     * 弹性域
     */
    private BigDecimal attribute3;

    /**
     * 弹性域
     */
    private BigDecimal attribute4;

    /**
     * 弹性域
     */
    private BigDecimal attribute5;

    /**
     * 实发工资
     */
    private BigDecimal netPayment;

    /**
     * 是否冻结
     */
    @NotNull
    private Boolean frozenFlag;

    // 安全累积奖
    private BigDecimal safetyAccumulationAward;

    // 编辑标记
    private Boolean checkFlag = false;

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
