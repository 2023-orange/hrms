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
 * 个人所得税表
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmPersonalIncomeTax extends BaseEntity {

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
     * 税款所属期
     */
    @NotBlank
    private String taxPeriod;

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
     * 本月收入
     */
    @NotNull
    private BigDecimal monthIncome;

    /**
     * 累计收入
     */
    @NotNull
    private BigDecimal amountIncome;

    /**
     * 累计社保减除
     */
//    @NotNull
    private BigDecimal amountSocialInsuranceDeduct;

    /**
     * 累计公积金减除
     */
//    @NotNull
    private BigDecimal amountAccumulationFundDeduct;

    /**
     * 累计专项附加减除
     */
//    @NotNull
    private BigDecimal amountSpecialAdditionalDeduct;

    /**
     * 累计起征点减除
     */
//    @NotNull
    private BigDecimal amountStartingPointDeduct;

    /**
     * 累计减除费用
     */
//    @NotNull
    private BigDecimal amountDeductExpenses;

    /**
     * 累计应纳税所得额
     */
    @NotNull
    private BigDecimal amountTaxableIncome;

    /**
     * 税率
     */
    @NotNull
    private BigDecimal taxRate;

    /**
     * 速算扣除数
     */
    @NotNull
    private BigDecimal quickCalculationDeduction;

    /**
     * 累计应缴税额
     */
    @NotNull
    private BigDecimal amountTaxDue;

    /**
     * 累计已缴税额
     */
//    @NotNull
    private BigDecimal amountTaxPaid;

    /**
     * 累计应补（退税额）
     */
    @NotNull
    private BigDecimal amountTaxRefund;

    @NotNull
    private Long seId;

    private Boolean enabledFlag;

}
