package com.sunten.hrms.swm.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 *  @author：liangjw
 *  @Date: 2020/12/24 13:34
 *  @Description: 二次调配列表返回的对象
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmFloatingWageSpecialDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 所得期间（格式：年.月）
    private String incomePeriod;

    // 工牌号
    private String workCard;

    // 员工姓名
    private String employeeName;

    // 部门
    private String department;

    // 科室
    private String administrativeOffice;

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

}
