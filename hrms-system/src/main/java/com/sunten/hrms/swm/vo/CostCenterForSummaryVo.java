package com.sunten.hrms.swm.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CostCenterForSummaryVo {
    // 所得期间
    private String incomePeriod;
    // 成本中心号
    private String costCenterNum;
    // 成本中心名称
    private String costCenter;
    // 服务部门
    private String serviceDepartment;
    // 计数项:姓名
    private String countName;
    // 一孩补贴合计
    private BigDecimal countOneChildSubsidy;
    // 安全累积奖合计
    private BigDecimal countSafetyAccumulationAward;
    // 工龄津贴合计
    private BigDecimal countSeniorityAllowance;
    // 高温补贴合计
    private BigDecimal countHighTemperatureSubsidy;
    // 搬迁补贴合计
    private BigDecimal countTransportationAllowance;
    // 岗位补贴合计
    private BigDecimal countPostSubsidy;
    // 加班工资合计
    private BigDecimal countOvertimePay;
    // 补贴扣除合计
    private BigDecimal countAllowanceDeduction;
    // 应发工资合计
    private BigDecimal countWagesPayable;
    // 扣除医药费合计
    private BigDecimal countDeductMedicalCosts;
    // 扣除水电房合计
    private BigDecimal countDeductHydropowerHouse;
    // 扣除所得税固定合计
    private BigDecimal countDeductIncomeTax;
    // 扣除保险个人合计
    private BigDecimal countPersonalDeductibles;
    // 扣除公积金个人合计
    private BigDecimal countPersonalDeductAccumulationFund;
    // 扣除其它合计
    private BigDecimal countDeductOther;
    // 实发工资合计
    private BigDecimal countNetPayment;
}
