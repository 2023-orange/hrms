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
public class AcAbnormalClockRecordDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 日期
    private LocalDate date;

    // 考勤处理记录id
    private Long acAttendanceRecordHistoryId;

    // 打卡时间
    private LocalTime clockTime;

    // 门禁控制器编号
    private String cardMachineNo;

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

    /**
     * 处理人
     */
    private Long disposeBy;

    /**
     * 处理日期
     */
    private LocalDateTime disposeTime;

    /**
     * 已处理标记
     */
    private Boolean disposeFlag;

    /**
     * 复核标记
     */
    private Boolean checkFlag;

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

    /**
     * 个人处理结果
     */
    private String disposeResult;

    /**
     * 个人处理原因
     */
    private String disposeDescribes;

    /**
     * 资料员处理结果
     */
    private String checkResult;

    /**
     * 资料员处理原因
     */
    private String checkDescribes;
    /**
     * 管理员处理结果
     */
    private String adminResult;

    /**
     * 管理员处理原因
     */
    private String adminDescribes;



    /**
     *  @author：liangjw
     * 领导打回意见
     */
    private String leaderResult;
    /**
     * OA审批单号
     */
    private String reqCode;

    /**
     * OA审批单状态
     */
    private String state;

    /**
     * oa单日期明细
     */
    private String oaRemark;


    /**
     *  @author：liangjw
     *  考勤异常状态码（具体请查阅字典ac_abnormal_record_code_status）
     */
    private String codeStatus;


    /**
     *  @author：liangjw
     *  @Date: 2021/2/3 8:34
     *  @Description: 标记区分该异常记录是普通人员还是管理人员
     */
    private Boolean recordIsChargeFlag;

    // 提交标识， 只有提交了， 领导、 管理员才能看到
    private Boolean commitFlag;

    /**
     *  @author：liangjw
     *  @Date: 2021/3/5 14:23
     *  @Description: 用于区分在有授权情况下，领导审批完是该结束还是交给考勤管理员审批
     */
    private Boolean recordPlusChargeFlag;

    private LocalDateTime selfDisposeTime; // 个人处理时间（车间为班组长处理时间）

    private Long leaderDisposeBy; // 处理领导的empid

    private LocalDateTime leaderDisposeTime; //  领导处理时间

    private LocalDateTime acAdminDisposeTime; // 考勤管理员处理时间

}
