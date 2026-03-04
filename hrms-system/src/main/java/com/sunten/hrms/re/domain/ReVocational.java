package com.sunten.hrms.re.domain;

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
 * 招聘职业资格表
 * </p>
 *
 * @author xukai
 * @since 2020-08-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class ReVocational extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 招聘id
     */
    @TableField(exist = false)
    private ReRecruitment recruitment;

    /**
     * 资格名称
     */
    @NotBlank
    private String vocationalName;

    /**
     * 级别
     */
    @NotBlank
    private String vocationalLevel;

    /**
     * 评定时间
     */
    @NotNull
    private LocalDate evaluationTime;

    /**
     * 发证机构
     */
    @NotBlank
    private String evaluationMechanism;

    /**
     * 有效期
     */
    @NotNull
    private LocalDate vocationalValidity;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 是否当前最高
     */
    @NotNull
    private Boolean newVocationalFlag;

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
