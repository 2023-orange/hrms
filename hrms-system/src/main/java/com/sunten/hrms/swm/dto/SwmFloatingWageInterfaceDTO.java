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
public class SwmFloatingWageInterfaceDTO extends BaseDTO {
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

    // 调配绩效工资
    private BigDecimal allocatePerformancePay;

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

    // 税前奖励扣发
    private BigDecimal preTaxWithheld;

    // 税后奖励扣发
    private BigDecimal afterTaxWithheld;

    // 生产系数
    private BigDecimal productionFactor;

    // 质量系数
    private BigDecimal qualityFactor;

    // 考核系数
    private BigDecimal assessmentCoefficient;

}
