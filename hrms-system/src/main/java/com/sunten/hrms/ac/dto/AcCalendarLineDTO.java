package com.sunten.hrms.ac.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
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
public class AcCalendarLineDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id
    private Long id;

    // 日历主表id
    private Long calendarHeaderId;

    // 日期
    private LocalDate nowDate;

    // 星期
    private String week;

    // 是否休息日
    private Boolean dayOffFlag;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 有效标记
    private Boolean enabledFlag;


}
