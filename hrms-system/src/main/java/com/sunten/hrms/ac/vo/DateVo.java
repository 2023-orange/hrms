package com.sunten.hrms.ac.vo;

import com.sunten.hrms.ac.domain.AcEmployeeAttendance;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DateVo {
    private LocalDate date; // 日历日期
    private List<AcEmployeeAttendanceVo> acEmployeeAttendances; // 排班
}
