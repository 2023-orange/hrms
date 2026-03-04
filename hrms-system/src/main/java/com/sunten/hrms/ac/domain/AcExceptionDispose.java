package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 考勤异常及处理表
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcExceptionDispose extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 员工id
     */
    @NotNull
    private Long employeeId;

    private PmEmployee employee;

    /**
     * 异常类型
     */
    @NotBlank
    private String exceptionType;

    /**
     * 异常产生日期
     */
    @NotNull
    private LocalDate exceptionDate;

    /**
     * 异常详细说明
     */
    private String exceptionDescribes;

    /**
     * 异常处理人id
     */
    @NotNull
    private Long disposeEmployeeId;

    /**
     * 异常处理日期
     */
    @NotNull
    private LocalDateTime disposeDate;

    /**
     * 异常处理结果
     */
    private String disposeResult;

    /**
     * 异常处理完成标记
     */
    @NotNull
    private Boolean disposeFlag;

    /**
     * 相关OA审批单号
     */
    private String reqCode;

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
    @NotNull
    private Boolean enabledFlag;


}
