package com.sunten.hrms.swm.dto;

    import java.math.BigDecimal;
    import com.baomidou.mybatisplus.annotation.TableField;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhoujy
 * @since 2023-04-10
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmSalataxDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private BigDecimal fdyfgz;

    private BigDecimal gdyfgz;

    private BigDecimal btYh;

    private BigDecimal kcBx;

    private BigDecimal jjje;

    private String sjsdqj;

    private String sjsdqj1;

    private String gph;

    private String xm;

    private String sdqj;

    private BigDecimal rmb;

    private BigDecimal jfye;

    private BigDecimal ynssde;

    private Float sl;

    private BigDecimal sskcs;

    private BigDecimal dksds;

    private BigDecimal dksds0701;


}
