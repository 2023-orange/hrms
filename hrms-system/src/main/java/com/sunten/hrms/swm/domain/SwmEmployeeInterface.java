package com.sunten.hrms.swm.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * 薪酬员工信息接口表
 * </p>
 *
 * @author liangjw
 * @since 2021-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmEmployeeInterface extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 人员信息上的人员id
     */
    @NotNull
    private Long employeeId;

    /**
     * 工牌号
     */
    @NotBlank
    private String workCard;

    /**
     * 姓名
     */
    @NotBlank
    private String name;

    /**
     * 数据分组id
     */
    @NotNull
    private Long groupId;

    /**
     * 操作码
     */
    @NotBlank
    private String operationCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 数据状态
     */
    @NotBlank
    private String dataStatus;

    /**
     * 包干工资
     */
    private BigDecimal lumpSumWage;

    /**
     * 岗位技能工资
     */
    private BigDecimal postSkillSalary;

    /**
     * 目标绩效工资
     */
    private BigDecimal targetPerformancePay;

    /**
     * 基本工资
     */
    private BigDecimal basePay;

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

    /**
     * 高温补贴
     */
    private BigDecimal highTemperatureSubsidy;

    // 岗位补贴
    private BigDecimal postSubsidy;

    // 工龄津贴
    private BigDecimal seniorityAllowance;

    // 搬迁交通补贴
    private BigDecimal transportationAllowance;

    // 补贴一孩
    private BigDecimal oneChildSubsidy;

    // 安全累积奖
    private BigDecimal safetyAccumulationAward;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Double id;


}
