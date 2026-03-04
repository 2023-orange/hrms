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
public class SwmBonusPaymentInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 数据分组id
    private Long groupId;

    // 奖金名称
    private String bonusName;

    // 员工id
    private Long employeeId;

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

    // 操作码
    private String operationCode;

    // 错误信息
    private String errorMsg;

    // 数据状态
    private String dataStatus;


    private Long bonusId;

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

    /**
     * 部门
     */
    private String department;

    /**
     * 科室
     */
    private String administrativeOffice;

    /**
     * 入职时间
     */
    private String entryTime;


}
