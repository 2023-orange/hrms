package com.sunten.hrms.swm.domain;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmMonthlyQuarterlyAssessmentNum {

    /**
     * 部门名
     */
    private String department;

    /**
     * 科室名
     */
    private String administrativeOffice;

    /**
     * 总人数
     */
    private Integer totalNum;

    /**
     * 考核等级可以为A的人数
     */
    private Integer actualNum;

    /**
     * 当前月度考核为A的人数
     */
    private Integer currentNum;

    private String workCard;

    private String deptId;

    private String assessmentMonth;

    // 是否为没数据限制（特殊化） 1为特殊化 0为非特殊化
    private Boolean noLimitDeptFlag;
}
