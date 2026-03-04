package com.sunten.hrms.ac.vo;

import com.sunten.hrms.ac.domain.AcEmployeeAttendance;
import lombok.Data;

import java.util.List;

@Data
public class AcEmployeeVo {
    private Long employeeId;
    private String employeeName;
    private String workCard;
    private List<AcEmployeeAttendanceVo> acEmployeeAttendances;
    /**
     *  @author：liangjw
     *  @Date: 2020/10/9 14:07
     *  @Description: 前台选择标记
     */
    private Boolean checkFlag;
    private String dept;
    private String department;
    private String team;
}
