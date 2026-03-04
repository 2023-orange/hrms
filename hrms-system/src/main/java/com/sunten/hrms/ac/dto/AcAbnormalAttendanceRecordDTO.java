package com.sunten.hrms.ac.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
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
public class AcAbnormalAttendanceRecordDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 开始日期
    private LocalDate startTime;

    // 结束日期
    private LocalDate endTime;

    // 执行状态
    private String executionStatus;

    // 执行时间
    private LocalDateTime executionTime;

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


}
