package com.sunten.hrms.swm.dto;

    import java.math.BigDecimal;
    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2021-09-13
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmEmployeeInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 人员信息上的人员id
    private Long employeeId;

    // 工牌号
    private String workCard;

    // 姓名
    private String name;

    // 数据分组id
    private Long groupId;

    // 操作码
    private String operationCode;

    // 错误信息
    private String errorMsg;

    // 数据状态
    private String dataStatus;

    // 包干工资
    private BigDecimal lumpSumWage;

    // 岗位技能工资
    private BigDecimal postSkillSalary;

    // 目标绩效工资
    private BigDecimal targetPerformancePay;

    // 基本工资
    private BigDecimal basePay;

    // 扣除保险(个人)
    private BigDecimal personalDeductibles;

    // 扣除保险(公司)
    private BigDecimal companyDeductibles;

    // 扣除公积金（个人）
    private BigDecimal personalDeductAccumulationFund;

    // 扣除公积金（公司）
    private BigDecimal companyDeductAccumulationFund;

    // 高温补贴
    private BigDecimal highTemperatureSubsidy;

    // 岗位补贴
    private BigDecimal postSubsidy;

    // 工龄津贴
    private BigDecimal seniorityAllowance;

    // 搬迁交通补贴
    private BigDecimal transportationAllowance;

    // 补贴一孩
    private BigDecimal oneChildSubsidy;

    // 安全累积奖
    private BigDecimal safetyAccumulationAward;

    private Double id;


}
