package com.sunten.hrms.ac.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import java.time.LocalTime;
    import java.util.List;

    import com.fasterxml.jackson.annotation.JsonInclude;
    import com.sunten.hrms.ac.domain.AcCalendarHeader;
    import com.sunten.hrms.ac.domain.AcRegime;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.fnd.domain.FndDept;
    import com.sunten.hrms.fnd.dto.FndDeptDTO;
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
public class AcDeptAttendanceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id
    private Long id;

    // 部门id
    private Long deptId;

    private FndDeptDTO fndDept;

    // 考勤制度ID
    private Long regimeId;

    private AcRegimeDTO acRegime;

    // 工作日历id
    private Long calendarHeaderId;

    private AcCalendarHeaderDTO acCalendarHeader;

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

    // 生效日期(填写加1日)
    private LocalDate takeEffectDate;

    // 失效日期(只看日期不看时分秒)
    private LocalDate invalidDate;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 有效标记
    private Boolean enabledFlag;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<AcDeptAttendanceDTO> children;
}
