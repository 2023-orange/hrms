package com.sunten.hrms.ac.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import java.time.LocalTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xukai
 * @since 2021-07-08
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AcBeLateDateDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 迟到日期
    private LocalDate beLateDate;

    // 迟到延迟时间，默认9:00
    private LocalTime beLateTime;

    // 年份
    private String beLateYear;

    // 月份
    private Integer beLateMonth;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    private Long id;

    // 生效标记
    private Boolean enabledFlag;
}
