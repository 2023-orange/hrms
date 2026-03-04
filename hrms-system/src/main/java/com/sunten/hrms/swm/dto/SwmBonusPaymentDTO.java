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
public class SwmBonusPaymentDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 入职时间
    private String entryTime;

    // 部门
    private String department;

    // 科室
    private String administrativeOffice;

    // 奖金名称
    private String bonusName;

    // 工牌号
    private String workCard;

    // 员工姓名
    private String employeeName;

    // 银行账户
    private String bankAccount;

    // 银行名称
    private String bankName;

    // 应发金额
    private BigDecimal payableAmount;

    // 实发金额_税前
    private BigDecimal amountPreTax;

    // 扣除所得税
    private BigDecimal deductIncomeTax;

    // 实发金额_税后
    private BigDecimal amountAfterTax;

    private Long seId;

    private Long bonusId;

    private Boolean enabledFlag;

    private Boolean grantFlag;

    /**
     * 成本中心名称
     */
    private String costCenter;

    /**
     * 成本中心号
     */
    private String costCenterNum;

    /**
     * 服务部门
     */
    private String serviceDepartment;


}
