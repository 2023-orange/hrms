package com.sunten.hrms.swm.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableField;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 旧的浮动工资表
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmFdgz extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    @NotNull
    private Integer id;

    @TableField("SDQJ")
    @NotBlank
    private String sdqj;

    @TableField("GPH")
    @NotBlank
    private String gph;

    @TableField("XM")
    @NotBlank
    private String xm;

    @TableField("SZBM")
    private String szbm;

    @TableField("SZKS")
    private String szks;

    @TableField("YHZH")
    private String yhzh;

    @TableField("TPGZ")
    private BigDecimal tpgz;

    @TableField("XDDJ")
    private BigDecimal xddj;

    @TableField("TJBS")
    private Boolean tjbs;

    @TableField("JLKF_SQ")
    @NotNull
    private BigDecimal jlkfSq;

    @TableField("JLKF_SH")
    @NotNull
    private BigDecimal jlkfSh;

    @TableField("KC_SDS")
    @NotNull
    private BigDecimal kcSds;

    @TableField("SFGZ")
    @NotNull
    private BigDecimal sfgz;

    @TableField("YFGZ")
    @NotNull
    private BigDecimal yfgz;

    @TableField("TPJZ")
    private BigDecimal tpjz;

    @TableField("BZMC")
    private String bzmc;

    //考核等级
    @TableField("assessment_level")
    private String assessmentLevel;
}
