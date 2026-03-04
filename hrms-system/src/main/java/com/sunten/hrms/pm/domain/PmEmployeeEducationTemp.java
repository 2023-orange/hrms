package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 教育信息临时表
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeEducationTemp extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 员工id
     */
//    @NotNull
//    private Long employeeId;
    @TableField(exist = false)
    private PmEmployee employee;
    /**
     * 教育id
     */
//    @NotNull
//    private Long educationId;
    @TableField(exist = false)
    private PmEmployeeEducation employeeEducation;

    /**
     * 毕业学校
     */
//    @NotBlank
    private String school;

    /**
     * 学历
     */
//    @NotBlank
    private String education;

    /**
     * 入学时间
     */
//    @NotNull
    private LocalDate enrollmentTime;

    /**
     * 毕业时间
     */
//    @NotNull
    private LocalDate graduationTime;

    /**
     * 是否最高学历
     */
//    @NotNull
    private Boolean newEducationFlag;

    /**
     * 专业
     */
//    @NotBlank
    private String specializedSubject;

    /**
     * 入学性质
     */
//    @NotBlank
    private String enrollment;

    /**
     * 操作标记
     */
    @NotBlank
    private String instructionsFlag;

    /**
     * 校核标记
     */
    @NotBlank
    private String checkFlag;

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
     * 有效标记默认值
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


}
