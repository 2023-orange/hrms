package com.sunten.hrms.ac.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author ljw
 * @since 2020-09-17
 */
@Data
public class AcDeptAttendanceQueryCriteria implements Serializable {
    /**
     *  @author：liangjw
     *  @Date: 2020/9/24 16:15
     *  @Description: deptId
     */
    private Long deptId;

    private Long calendarHeaderId;

    private Long regimeId;

    private Long id;

    private Boolean enabled;

    private String deptName;

    /**
     *  @author：liangjw
     *  @Date: 2020/9/27 11:11
     *  @Description: 查全部历史的标记
     */
    private Boolean history;

    /**
     *  @author：liangjw
     *  @Date: 2020/10/10 15:27
     *  @Description: 查询某一天的部门考勤所用的参数
     */
    private LocalDate date;

    /**
     *  @author：liangjw
     *  @Date: 2020/10/13 8:50
     *  @Description: 查询当前及之后
     */
    private Boolean nowAndAfter;
}
