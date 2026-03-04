package com.sunten.hrms.re.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class ThreeYearCount {
    private Long deptId;
    private Long jobId;
    // 近一年
    private Integer firstTotal;
    // 近两年
    private Integer secondTotal;
    // 近三年
    private Integer thirdTotal;
    // 当月
    private Integer currentTotal;
    private List<ThreeYearCount> jobThreeYearCount;
}
