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
 * @since 2023-04-11
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmSalatax2006DTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Integer xh;

    private String ksqj;

    private String sdqj;

    private String gph;

    private String sfzh;

    private String xm;

    private Float srze;

    private Float fdfykce;

    private Float ynssde;

    private Float sl;

    private Float sskcs;

    private BigDecimal ynseNull;

    private Float ynseZe;

}
