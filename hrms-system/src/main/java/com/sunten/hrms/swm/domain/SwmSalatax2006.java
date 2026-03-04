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
 * 年底奖金所得税
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmSalatax2006 extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("XH")
    private Integer xh;

    @TableField("KSQJ")
    private String ksqj;

    @TableField("SDQJ")
    private String sdqj;

    private String gph;

    @TableField("SFZH")
    private String sfzh;

    @TableField("XM")
    private String xm;

    @TableField("SRZE")
    private Float srze;

    @TableField("FDFYKCE")
    private Float fdfykce;

    @TableField("YNSSDE")
    private Float ynssde;

    @TableField("SL")
    private Float sl;

    @TableField("SSKCS")
    private Float sskcs;

    @TableField("YNSE_NULL")
    private BigDecimal ynseNull;

    @TableField("YNSE_ZE")
    private Float ynseZe;

}
