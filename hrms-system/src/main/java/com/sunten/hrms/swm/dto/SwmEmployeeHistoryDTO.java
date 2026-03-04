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
public class SwmEmployeeHistoryDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 工牌号
    private String workCard;

    // 员工姓名
    private String employeeName;

    // 岗位技能工资
    private BigDecimal postSkillSalary;

    // 目标绩效工资
    private BigDecimal targetPerformancePay;

    // 包干区分（1包干，0非包干）
    private Boolean divisionContractFlag;

    // 变化幅度
    private BigDecimal rangeChange;

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

    private String remarks;

    /**
     * 岗位补贴
     */
    private BigDecimal postSubsidy;

    /**
     * 工龄津贴
     */
    private BigDecimal seniorityAllowance;

    /**
     * 扣除保险(个人)
     */
    private BigDecimal personalDeductibles;

    /**
     * 扣除保险(公司)
     */
    private BigDecimal companyDeductibles;

    /**
     * 扣除公积金（个人）
     */
    private BigDecimal personalDeductAccumulationFund;

    /**
     * 扣除公积金（公司）
     */
    private BigDecimal companyDeductAccumulationFund;

    // 包干工资
    private BigDecimal lumpSumWage;

    // 基本工资
    private BigDecimal basePay;
}
