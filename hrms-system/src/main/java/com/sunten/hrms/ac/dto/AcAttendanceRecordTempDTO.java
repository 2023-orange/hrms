package com.sunten.hrms.ac.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.LocalTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2020-10-15
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AcAttendanceRecordTempDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 异常考勤执行记录id
    private Long abnromalAttendanceRecordId;

    // 员工id
    private Long employeeId;

    // 日期
    private LocalDate date;

    // 上一段的结束时间（空意味上一天是休息日，非空意味上一天非休息日，时间为上一天的最后一段的结束时间）
    private LocalDateTime lastParagraphEndTime;

    // 一班时间开始
    private LocalTime firstTimeFrom;

    // 一班时间结束
    private LocalTime firstTimeTo;

    // 一班是否跨日
    private Boolean extend1TimeFlag;

    // 二班时间开始
    private LocalTime secondTimeFrom;

    // 二班时间结束
    private LocalTime secondTimeTo;

    // 二班是否跨日
    private Boolean extend2TimeFlag;

    // 三班时间开始
    private LocalTime thirdTimeFrom;

    // 三班时间结束
    private LocalTime thirdTimeTo;

    // 三班是否跨日
    private Boolean extend3TimeFlag;

    // 下一段的开始时间（空意味着下一天没排班或者位休息日，非空下一天为工作日，时间为下一天的第一段的开始时间）
    private LocalDateTime nextPeriodStartTime;

    // 是否休息
    private Boolean restFlag;

    // 是否无排班（异常标记）
    private Boolean noScheduling;

    // 是否休息日打卡（异常标记）
    private Boolean restDayClock;

    // 是否全天未打卡（异常标记）
    private Boolean allDayNoClock;

    // 是否上班时间打卡异常（异常标记）
    private Boolean officeTimeClock;

    // 是否上班未打卡（异常标记）
    private Boolean noClockIn;

    // 是否上下班打卡次数异常（异常标记）
    private Boolean abnormalClockInTime;

    // 是否下班未打卡异常（异常标记）
    private Boolean noClockAfterWork;

    // 一班时间开始修正
    private LocalTime altFirstTimeFrom;

    // 一班时间结束修正
    private LocalTime altFirstTimeTo;

    // 一班是否跨日修正
    private Boolean altExtend1TimeFlag;

    // 二班时间开始修正
    private LocalTime altSecondTimeFrom;

    // 二班时间结束修正
    private LocalTime altSecondTimeTo;

    // 二班是否跨日修正
    private Boolean altExtend2TimeFlag;

    // 三班时间开始修正
    private LocalTime altThirdTimeFrom;

    // 三班时间结束修正
    private LocalTime altThirdTimeTo;

    // 三班是否跨日修正
    private Boolean altExtend3TimeFlag;

    // 是否休息修正
    private Boolean altRestDayFlag;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 弹性域4
    private String attribute4;

    // 弹性域5
    private String attribute5;

    private Long id;


}
