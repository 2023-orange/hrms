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
 * 旧的固定工资表
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmGdgz extends BaseEntity {

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

    @TableField("BGQF")
    private Boolean bgqf;

    @TableField("SZBM")
    private String szbm;

    @TableField("SZKS")
    private String szks;

    @TableField("YHZH")
    private String yhzh;

    @TableField("BT_YH")
    @NotNull
    private BigDecimal btYh;

    @TableField("BTKC_SQ")
    @NotNull
    private BigDecimal btkcSq;

    @TableField("KC_YYF")
    @NotNull
    private BigDecimal kcYyf;

    @TableField("KC_SDF")
    @NotNull
    private BigDecimal kcSdf;

    @TableField("KC_BX")
    @NotNull
    private BigDecimal kcBx;

    @TableField("KC_SDS")
    @NotNull
    private BigDecimal kcSds;

    @TableField("KC_QT")
    @NotNull
    private BigDecimal kcQt;

    @TableField("KC_LF")
    @NotNull
    private BigDecimal kcLf;

    @TableField("JBGZ")
    @NotNull
    private BigDecimal jbgz;

    @TableField("YFGZ")
    private BigDecimal yfgz;

    @TableField("SFGZ")
    @NotNull
    private BigDecimal sfgz;

    @TableField("BZMC")
    private String bzmc;

    @TableField("JBGZ_input")
    private BigDecimal jbgzInput;


}
