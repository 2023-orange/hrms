package com.sunten.hrms.td.vo;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;


@Data
@ToString(callSuper = true)
public class PmPlanHistoryVo {
    private String name;

    private String workCard;

    private String peId;

    private Double sumDuration; // 总学习时长

    private Long participateNum; // 总培训次数

    private String trainingName; // 培训名称

    private String trainingMethod; // 培训类别

    private String trainingContent; // 培训内容

    private String teacher; // 主讲人

    private LocalDate beginDate;

    private LocalDate endDate;

    private String trainingAddress; // 培训地点

    private Double trainingTimeQuantity; // 培训预设学时

    private Double duration; // 实际出勤学时

    private Double grade;

    private Double evaluate; // 满意度

    private String deptName;

    private String department;

    private String team;

}
