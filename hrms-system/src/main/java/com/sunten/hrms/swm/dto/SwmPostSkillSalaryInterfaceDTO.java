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
public class SwmPostSkillSalaryInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 数据分组id
    private Long groupId;

    // 员工id
    private Long employeeId;

    // 所得期间（格式：年.月）
    private String incomePeriod;

    // 工牌号
    private String workCard;

    // 员工姓名
    private String employeeName;

    // 工作日加班工资
    private BigDecimal overtimePay;

    // 法定节假日加班工资
    private BigDecimal holidayOvertimePay;

    // 休息日加班工资
    private BigDecimal restOvertimePay;

    // 操作码
    private String operationCode;

    // 错误信息
    private String errorMsg;

    // 数据状态
    private String dataStatus;

    // 弹性域
    private BigDecimal attribute1;

    // 弹性域
    private BigDecimal attribute2;

    // 弹性域
    private BigDecimal attribute3;

    // 弹性域
    private BigDecimal attribute4;

    // 弹性域
    private BigDecimal attribute5;

    // 弹性域
    private BigDecimal attribute6;

    // 弹性域
    private BigDecimal attribute7;

    // 弹性域
    private BigDecimal attribute8;

    // 弹性域
    private BigDecimal attribute9;

    private BigDecimal deductIncomeTax;

    private BigDecimal safetyAccumulationAward;

    private BigDecimal highTemperatureSubsidy;

    private BigDecimal allowanceDeduction;

    private BigDecimal deductOther;

    private BigDecimal overtimePayTime;

    private BigDecimal holidayOvertimePayTime;

    private BigDecimal restOvertimePayTime;

    private BigDecimal transportationAllowance;

    private BigDecimal deductHydropowerHouse;

    private Boolean specialImportFlag;
}
