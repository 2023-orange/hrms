package com.sunten.hrms.swm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 员工信息历史表
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmEmployeeHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

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
     * 岗位技能工资
     */
    @NotNull
    private BigDecimal postSkillSalary;

    /**
     * 目标绩效工资
     */
    @NotNull
    private BigDecimal targetPerformancePay;

    /**
     * 包干区分（1包干，0非包干）
     */
    @NotNull
    private Boolean divisionContractFlag;

    /**
     * 变化幅度
     */
    @NotNull
    private BigDecimal rangeChange;

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

    private String remarks;
    // 薪酬人员Id
    private Long seId;

    // 包干工资
    private BigDecimal lumpSumWage;

    // 基本工资
    private BigDecimal basePay;

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
}
