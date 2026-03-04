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
 * 浮动工资表
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmFloatingWage extends BaseEntity {

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
     * 生产区分（1生产，0非生产）
     */
    @NotNull
    private Boolean generationDifferentiationFlag;

    /**
     * 部门
     */
    @NotBlank
    private String department;

    /**
     * 科室
     */
    private String administrativeOffice;

    /**
     * 分配方式名称
     */
    @NotBlank
    private String distributionMethod;

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
     * 考核形式（月度考核、季度考核、无考核）
     */
    @NotBlank
    private String accessmentForm;

    /**
     * 目标绩效工资
     */
    @NotNull
    private BigDecimal targetPerformancePay;

    /**
     * 生产系数
     */
    @NotNull
    private BigDecimal productionFactor;

    /**
     * 质量系数
     */
    @NotNull
    private BigDecimal qualityFactor;

    /**
     * 考核系数
     */
    @NotNull
    private BigDecimal assessmentCoefficient;

    /**
     * 月绩效工资
     */
    @NotNull
    private BigDecimal monthlyPerformanceSalary;

    /**
     * 调配绩效工资（车间导入）
     */
    private BigDecimal allocatePerformancePay;

    /**
     * 税前奖励扣发
     */
    @NotNull
    private BigDecimal preTaxWithheld;

    /**
     * 应发工资
     */
    @NotNull
    private BigDecimal wagesPayable;

    /**
     * 税后奖励扣发
     */
    @NotNull
    private BigDecimal afterTaxWithheld;

    /**
     * 实发工资
     */
    @NotNull
    private BigDecimal netPayment;

    /**
     * 提交标识（	   1提交，0未提交）
     */
    @NotNull
    private Boolean commitFlag;

    /**
     * 是否冻结
     */
    @NotNull
    private Boolean frozenFlag;

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


    @NotNull
    private Long seId;

    private Boolean enabledFlag;

    private Boolean grantFlag;

    private Boolean checkFlag = false; // 编辑标记

    private String updateByStr;
}
