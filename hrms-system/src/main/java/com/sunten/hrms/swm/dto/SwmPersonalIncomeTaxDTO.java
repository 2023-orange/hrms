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
public class SwmPersonalIncomeTaxDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 所得期间（格式：年.月）
    private String incomePeriod;

    // 税款所属期
    private String taxPeriod;

    // 工牌号
    private String workCard;

    // 员工姓名
    private String employeeName;

    // 本月收入
    private BigDecimal monthIncome;

    // 累计收入
    private BigDecimal amountIncome;

    // 累计社保减除
    private BigDecimal amountSocialInsuranceDeduct;

    // 累计公积金减除
    private BigDecimal amountAccumulationFundDeduct;

    // 累计专项附加减除
    private BigDecimal amountSpecialAdditionalDeduct;

    // 累计起征点减除
    private BigDecimal amountStartingPointDeduct;

    // 累计减除费用
    private BigDecimal amountDeductExpenses;

    // 累计应纳税所得额
    private BigDecimal amountTaxableIncome;

    // 税率
    private BigDecimal taxRate;

    // 速算扣除数
    private BigDecimal quickCalculationDeduction;

    // 累计应缴税额
    private BigDecimal amountTaxDue;

    // 累计已缴税额
    private BigDecimal amountTaxPaid;

    // 累计应补（退税额）
    private BigDecimal amountTaxRefund;

    private Long seId;

    private Boolean enabledFlag;

    private Boolean amountFlag;


}
