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
 * 职业资格临时表
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeVocationalTemp extends BaseEntity {

    private static final long serialVersionUID = 1L;

//    @NotNull
//    private Long employeeId;
    @TableField(exist = false)
    private PmEmployee employee;
    /**
     * 资格id
     */
//    @NotNull
//    private Long vocationalId;
    @TableField(exist = false)
    private PmEmployeeVocational employeeVocational;

    /**
     * 资格名称
     */
//    @NotBlank
    private String vocationalName;

    /**
     * 级别
     */
//    @NotBlank
    private String vocationalLevel;

    /**
     * 评定时间
     */
//    @NotNull
    private LocalDate evaluationTime;

    /**
     * 发证机构
     */
//    @NotBlank
    private String evaluationMechanism;

    /**
     * 有效期
     */
//    @NotNull
    private LocalDate vocationalValidity;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 是否当前最高
     */
//    @NotNull
    private Boolean newVocationalFlag;

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

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    @NotNull
    private Boolean enabledFlag;


}
