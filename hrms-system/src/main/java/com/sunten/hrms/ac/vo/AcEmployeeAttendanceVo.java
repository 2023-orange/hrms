package com.sunten.hrms.ac.vo;

import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
//@AllArgsConstructor
public class AcEmployeeAttendanceVo {
    private Long id;

    private Long employeeId;

    private String employeeName;

    private LocalDate regimeaDate;

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
     * 有效标记
     */
    private Boolean enabledFlag;
    /**
     *  @author：liangjw
     *  @Date: 2020/9/27 16:19
     *  @Description: 是否休息日，默认0
     */
    private Boolean restDayFlag;

    /**
     *  @author：liangjw
     *  @Date: 2020/10/19 16:27
     *  @Description: 仅用于考勤异常生成
     */
    private Boolean AttendanceFlag;

}
