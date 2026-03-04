package com.sunten.hrms.ac.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import java.time.LocalDateTime;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
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
public class AcExceptionDisposeDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id
    private Long id;

    // 员工id
    private Long employeeId;

    private PmEmployeeDTO employee;

    // 异常类型
    private String exceptionType;

    // 异常产生日期
    private LocalDate exceptionDate;

    // 异常详细说明
    private String exceptionDescribes;

    // 异常处理人id
    private Long disposeEmployeeId;

    // 异常处理日期
    private LocalDateTime disposeDate;

    // 异常处理结果
    private String disposeResult;

    // 异常处理完成标记
    private Boolean disposeFlag;

    // 相关OA审批单号
    private String reqCode;
    // 审批单号信息
    private AcVacateDTO vacate;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 有效标记
    private Boolean enabledFlag;


}
