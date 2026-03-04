package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 考勤处理记录历史表
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcAttendanceRecordHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 异常考勤执行记录id
     */
    @NotNull
    private Long abnromalAttendanceRecordId;

    /**
     * 员工id
     */
    @NotNull
    private Long employeeId;

    /**
     * 日期
     */
    @NotNull
    private LocalDate date;

    /**
     * 上一段的结束时间（空意味上一天是休息日，非空意味上一天非休息日，时间为上一天的最后一段的结束时间）
     */
    private LocalDateTime lastParagraphEndTime;

    /**
     * 一班时间开始
     */
    private LocalTime firstTimeFrom;

    /**
     * 一班时间结束
     */
    private LocalTime firstTimeTo;

    /**
     * 一班是否跨日
     */
    private Boolean extend1TimeFlag;

    /**
     * 二班时间开始
     */
    private LocalTime secondTimeFrom;

    /**
     * 二班时间结束
     */
    private LocalTime secondTimeTo;

    /**
     * 二班是否跨日
     */
    private Boolean extend2TimeFlag;

    /**
     * 三班时间开始
     */
    private LocalTime thirdTimeFrom;

    /**
     * 三班时间结束
     */
    private LocalTime thirdTimeTo;

    /**
     * 三班是否跨日
     */
    private Boolean extend3TimeFlag;

    /**
     * 下一段的开始时间（空意味着下一天没排班或者位休息日，非空下一天为工作日，时间为下一天的第一段的开始时间）
     */
    private LocalDateTime nextPeriodStartTime;

    /**
     * 是否休息
     */
    private Boolean restFlag;

    /**
     * 是否无排班（异常标记）
     */
    private Boolean noScheduling;

    /**
     * 是否休息日打卡（异常标记）
     */
    private Boolean restDayClock;

    /**
     * 是否全天未打卡（异常标记）
     */
    private Boolean allDayNoClock;

    /**
     * 是否上班时间打卡异常（异常标记）
     */
    private Boolean officeTimeClock;

    /**
     * 是否上班未打卡（异常标记）
     */
    private Boolean noClockIn;

    /**
     * 是否上下班打卡次数异常（异常标记）
     */
    private Boolean abnormalClockInTime;

    /**
     * 是否下班未打卡异常（异常标记）
     */
    private Boolean noClockAfterWork;

    /**
     * 一班时间开始修正
     */
    private LocalTime altFirstTimeFrom;

    /**
     * 一班时间结束修正
     */
    private LocalTime altFirstTimeTo;

    /**
     * 一班是否跨日修正
     */
    private Boolean altExtend1TimeFlag;

    /**
     * 二班时间开始修正
     */
    private LocalTime altSecondTimeFrom;

    /**
     * 二班时间结束修正
     */
    private LocalTime altSecondTimeTo;

    /**
     * 二班是否跨日修正
     */
    private Boolean altExtend2TimeFlag;

    /**
     * 三班时间开始修正
     */
    private LocalTime altThirdTimeFrom;

    /**
     * 三班时间结束修正
     */
    private LocalTime altThirdTimeTo;

    /**
     * 三班是否跨日修正
     */
    private Boolean altExtend3TimeFlag;

    /**
     * 是否休息修正
     */
    private Boolean altRestDayFlag;

    /**
     * 弹性域1
     */
    private String attribute1;

    /**
     * 弹性域2
     */
    private String attribute2;

    /**
     * 弹性域3
     */
    private String attribute3;

    /**
     * 弹性域4
     */
    private String attribute4;

    /**
     * 弹性域5
     */
    private String attribute5;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private String disposeFlag;

    // 处理结果
    private String result;

    /**
     * 员工信息
     */
    private PmEmployee employee;

    /**
     * 打卡记录， 定时任务时使用
     */
    private List<AcAbnormalClockRecord> clockRecords;

    /**
     *  @author：liangjw
     *  @Date: 2020/11/6 16:41
     *  @Description: 异常打卡记录
     */
    private AcAbnormalClockRecord abClockRecord;

    private Boolean editFlag = false; // 编辑标记

    private Boolean docFlag;

    /**
     * 个人处理结果
     */
    private String disposeResult;
    /**
     * 个人处理原因
     */
    private String disposeDescribes;
    /**
     * OA审批单号
     */
    private String reqCode;
    /**
     * 处理人
     */
    private Long disposeBy;
    /**
     * 处理日期
     */
    private LocalDateTime disposeTime;
    /**
     * OA审批单状态
     */
    private String state;
    /**
     * 管理员打回原因
     */
    private String adminResult;

    /**
     * 管理员处理原因
     */
    private String adminDescribes;
    /**
     * 资料员处理结果
     */
    private String checkResult;

    /**
     * 资料员处理原因
     */
    private String checkDescribes;
    /**
     * oa单日期明细
     */
    private String oaRemark;
    /**
     * 领导打回意见
     */
    private String leaderResult;
    /**
     * 提交标识， 只有提交了， 领导、 管理员才能看到
     */
    private Boolean commitFlag;

    /**
     * 个人处理时间（车间为班组长处理时间）
     */
    private LocalDateTime selfDisposeTime;

    /**
     * 处理领导的empid
     */
    private Long leaderDisposeBy;

    /**
     * 领导处理时间
     */
    private LocalDateTime leaderDisposeTime;

    /**
     * 考勤管理员处理时间
     */
    private LocalDateTime acAdminDisposeTime;
    /**
     * 一日的打卡时间字符串
     */
    private String dayClockTime;

    /**
     * 管理人员标记
     */
    private Boolean chargeFlag;

    /**
     *  @author：liangjw
     *  @Date: 2021/3/5 14:23
     *  @Description: 用于区分在有授权情况下，领导审批完是该结束还是交给考勤管理员审批
     */
    private Boolean recordPlusChargeFlag;

    /**
     *  @author：liangjw
     *  前台最终使用的处理结果
     */
    private String targetResult;

    /**
     *  @author：liangjw
     *  前台最终使用的处理原因
     */
    private String targetDescribes;

    private String codeStatus;

    private Boolean recordIsChargeFlag;


}
