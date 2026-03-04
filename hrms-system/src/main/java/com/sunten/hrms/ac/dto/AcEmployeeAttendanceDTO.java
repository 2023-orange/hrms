package com.sunten.hrms.ac.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import java.time.LocalTime;
    import java.util.List;

    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.pm.domain.PmEmployee;
    import com.sunten.hrms.pm.dto.PmEmployeeDTO;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ljw
 * @since 2020-09-17
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AcEmployeeAttendanceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id
    private Long id;

    // 员工人事档案id
    private Long employeeId;

    private PmEmployeeDTO employee;

    // 排班日期
    private LocalDate regimeaDate;

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

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 有效标记
    private Boolean enabledFlag;

    // 是否休息日
    private Boolean restDayFlag;

    //
    private List<AcEmployeeAttendanceDTO> acEmployeeAttendances;


}
