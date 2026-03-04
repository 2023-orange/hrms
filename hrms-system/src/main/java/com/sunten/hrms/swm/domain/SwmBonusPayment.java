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
 * 奖金发放表
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmBonusPayment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 入职时间
     */
    private String entryTime;

    /**
     * 部门
     */
    private String department;

    /**
     * 科室
     */
    private String administrativeOffice;

    /**
     * 奖金名称
     */
    @NotBlank
    private String bonusName;

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
     * 银行账户
     */
    private String bankAccount;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 应发金额
     */
    @NotNull
    private BigDecimal payableAmount;

    /**
     * 实发金额_税前
     */
    private BigDecimal amountPreTax;

    /**
     * 扣除所得税
     */
    private BigDecimal deductIncomeTax;

    /**
     * 实发金额_税后
     */
    private BigDecimal amountAfterTax;

    @NotNull
    private Long seId;


    @NotNull
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
