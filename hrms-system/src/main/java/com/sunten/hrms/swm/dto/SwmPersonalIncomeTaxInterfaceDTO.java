package com.sunten.hrms.swm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.math.BigDecimal;
    import java.time.LocalDate;

/**
 * @author liangjw
 * @since 2021-01-14
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmPersonalIncomeTaxInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 数据分组id
    private Long groupId;

    // 员工id
    private Long employeeId;

    // 税款所属期起
    private LocalDate taxPeriodStart;

    // 税款所属期止
    private LocalDate taxPeriodEnd;

    // 本期基本养老保险费
    private Double currentBasicPensionInsurance;

    // 本期基本医疗保险费
    private Double currentBasicMedical;

    // 本期失业保险费
    private Double currentUnemployment;

    // 本期住房公积金
    private Double currentHousingAccumulationFund;

    // 工牌号
    private String workCard;

    // 员工姓名
    private String employeeName;

    // 本月收入(本期收入)
    private Double monthIncome;

    // 累计收入
    private Double amountIncome;

    // 累计减除费用
    private Double amountDeductExpenses;

    // 累计应纳税所得额
    private Double amountTaxableIncome;

    // 税率
    private Double taxRate;

    // 速算扣除数
    private Double quickCalculationDeduction;

    // 累计应纳税额
    private Double amountTaxDue;

    // 累计已缴税额
    private Double amountTaxPaid;

    // 累计应补（退税额）
    private Double amountTaxRefund;

    // 操作码
    private String operationCode;

    // 错误信息
    private String errorMsg;

    // 数据状态
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

    private String incomePeriod;

    private String taxPeriod;

}
