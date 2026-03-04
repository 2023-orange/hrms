package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;

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
 * 职称情况临时表
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeTitleTemp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 职称id
     */
//    @NotNull
//    private Long titleId;
    @TableField(exist = false)
    private PmEmployeeTitle employeeTitle;

    /**
     * 职称级别
     */
//    @NotBlank
    private String titleLevel;

    /**
     * 职称名称
     */
//    @NotBlank
    private String titleName;

    /**
     * 评定时间
     */
//    @NotNull
    private LocalDate evaluationTime;

    /**
     * 是否最高职称
     */
//    @NotNull
    private Boolean newTitleFlag;

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
    /**
     * 员工id
     */
    @NotNull
    private Long employeeId;
}
