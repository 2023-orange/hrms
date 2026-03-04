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
 * @since 2023-04-07
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmGdgzDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String sdqj;

    private String gph;

    private String xm;

    private Boolean bgqf;

    private String szbm;

    private String szks;

    private String yhzh;

    private BigDecimal btYh;

    private BigDecimal btkcSq;

    private BigDecimal kcYyf;

    private BigDecimal kcSdf;

    private BigDecimal kcBx;

    private BigDecimal kcSds;

    private BigDecimal kcQt;

    private BigDecimal kcLf;

    private BigDecimal jbgz;

    private BigDecimal yfgz;

    private BigDecimal sfgz;

    private String bzmc;

    private BigDecimal jbgzInput;


}
