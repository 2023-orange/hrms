package com.sunten.hrms.td.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 参训人员表（包括讲师）
 * </p>
 *
 * @author liangjw
 * @since 2021-05-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdPlanEmployee extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 培训计划实施id
     */
    @NotNull
    private Long planImplementId;

    /**
     * 人员id
     */
    @NotNull
    private Long employeeId;

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;

    /**
     * employee、teacher
     */
    @NotBlank
    private String type;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


    private String employeeName;

    private String email;

    private Long deptId;

    private Boolean workshopAttendanceFlag; // 排班标记

    private String planName;

}
