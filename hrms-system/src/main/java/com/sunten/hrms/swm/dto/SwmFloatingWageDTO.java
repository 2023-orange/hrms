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
public class SwmFloatingWageDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 所得期间（格式：年.月）
    private String incomePeriod;

    // 工牌号
    private String workCard;

    // 员工姓名
    private String employeeName;

    // 员工类别
    private String employeeCategory;

    // 包干区分（1包干，0非包干）
    private Boolean divisionContractFlag;

    // 生产区分（1生产，0非生产）
    private Boolean generationDifferentiationFlag;

    // 部门
    private String department;

    // 科室
    private String administrativeOffice;

    // 分配方式名称
    private String distributionMethod;

    // 银行账号
    private String bankAccount;

    // 开户行名称
    private String bankName;

    // 考核形式（月度考核、季度考核、无考核）
    private String accessmentForm;

    // 目标绩效工资
    private BigDecimal targetPerformancePay;

    // 生产系数
    private BigDecimal productionFactor;

    // 质量系数
    private BigDecimal qualityFactor;

    // 考核系数
    private BigDecimal assessmentCoefficient;

    // 月绩效工资
    private BigDecimal monthlyPerformanceSalary;

    // 调配绩效工资（车间导入）
    private BigDecimal allocatePerformancePay;

    // 税前奖励扣发
    private BigDecimal preTaxWithheld;

    // 应发工资
    private BigDecimal wagesPayable;

    // 税后奖励扣发
    private BigDecimal afterTaxWithheld;

    // 实发工资
    private BigDecimal netPayment;

    // 提交标识（	   1提交，0未提交）
    private Boolean commitFlag;

    // 是否冻结
    private Boolean frozenFlag;

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

    private Long seId;

    private Boolean enabledFlag;

    private Boolean grantFlag;

    private Boolean checkFlag = false; // 编辑标记

    private String updateByStr;

}
