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
 * 岗位技能工资接口表
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmPostSkillSalaryInterface extends BaseEntity {

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
     * 工作日加班工资
     */
    private BigDecimal overtimePay;

    /**
     * 法定节假日加班工资
     */
    private BigDecimal holidayOvertimePay;

    /**
     * 休息日加班工资
     */
    private BigDecimal restOvertimePay;

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

    /**
     * 弹性域
     */
    private BigDecimal attribute6;

    /**
     * 弹性域
     */
    private BigDecimal attribute7;

    /**
     * 弹性域
     */
    private BigDecimal attribute8;

    /**
     * 弹性域
     */
    private BigDecimal attribute9;

    // 导入人员工ID
    private Long userEmpId;

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
