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
import java.time.LocalDate;

/**
 * <p>
 * 个人所得税接口表
 * </p>
 *
 * @author liangjw
 * @since 2021-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmPersonalIncomeTaxInterface extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 数据分组id
     */
    @NotNull
    private Long groupId;

    /**
     * 员工id
     */
    @NotNull
    private Long employeeId;

    /**
     * 税款所属期起
     */
//    @NotBlank
    private LocalDate taxPeriodStart;

    /**
     * 税款所属期止
     */
//    @NotBlank
    private LocalDate taxPeriodEnd;

    /**
     * 本期基本养老保险费
     */
    private BigDecimal currentBasicPensionInsurance;

    /**
     * 本期基本医疗保险费
     */
    private BigDecimal currentBasicMedical;

    /**
     * 本期失业保险费
     */
    private BigDecimal currentUnemployment;

    /**
     * 本期住房公积金
     */
    private BigDecimal currentHousingAccumulationFund;

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
     * 本月收入(本期收入)
     */
    private BigDecimal monthIncome;

    /**
     * 累计收入
     */
    private BigDecimal amountIncome;

    /**
     * 累计减除费用
     */
    private BigDecimal amountDeductExpenses;

    /**
     * 累计应纳税所得额
     */
    private BigDecimal amountTaxableIncome;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 速算扣除数
     */
    private BigDecimal quickCalculationDeduction;

    /**
     * 累计应纳税额
     */
    private BigDecimal amountTaxDue;

    /**
     * 累计已缴税额
     */
    private BigDecimal amountTaxPaid;

    /**
     * 累计应补（退税额）
     */
    private BigDecimal amountTaxRefund;

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
    // 累计子女教育支出扣除
    private BigDecimal deductSumChildEducation;
    // 累计继续教育支出扣除
    private BigDecimal deductSumEducationContinue;
    // 累计住房贷款利息支出扣除
    private BigDecimal deductSumHouseLoan;
    // 累计住房租金支出扣除
    private BigDecimal deductSumHousingRental;
    // 累计赡养老人支出扣除
    private BigDecimal deductSumSupportElderly;
    // 累计3岁以下婴幼儿照护
    private BigDecimal deductSumThreeChild;

    private Long userId;

    private String incomePeriod;

    private String taxPeriod;

    private String taxPeriodStartStr;

    private String taxPeriodEndStr;

    private Boolean amountFlag;
}
