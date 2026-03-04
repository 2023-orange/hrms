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
 * 旧的所得税表
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmSalatax extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    @NotNull
    private Integer id;

    @TableField("FDYFGZ")
    private BigDecimal fdyfgz;

    @TableField("GDYFGZ")
    private BigDecimal gdyfgz;

    @TableField("BT_YH")
    private BigDecimal btYh;

    @TableField("kc_BX")
    private BigDecimal kcBx;

    @TableField("JJJE")
    private BigDecimal jjje;

    @TableField("SJSDQJ")
    private String sjsdqj;

    @TableField("SJSDQJ1")
    private String sjsdqj1;

    @TableField("GPH")
    private String gph;

    @TableField("XM")
    private String xm;

    @TableField("SDQJ")
    private String sdqj;

    @TableField("RMB")
    private BigDecimal rmb;

    @TableField("JFYE")
    private BigDecimal jfye;

    @TableField("YNSSDE")
    private BigDecimal ynssde;

    @TableField("SL")
    private Float sl;

    @TableField("SSKCS")
    private BigDecimal sskcs;

    @TableField("DKSDS")
    private BigDecimal dksds;

    @TableField("DKSDS0701")
    private BigDecimal dksds0701;


}
