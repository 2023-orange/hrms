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
public class SwmFdgzDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String sdqj;

    private String gph;

    private String xm;

    private String szbm;

    private String szks;

    private String yhzh;

    private BigDecimal tpgz;

    private BigDecimal xddj;

    private Boolean tjbs;

    private BigDecimal jlkfSq;

    private BigDecimal jlkfSh;

    private BigDecimal kcSds;

    private BigDecimal sfgz;

    private BigDecimal yfgz;

    private BigDecimal tpjz;

    private String bzmc;

    //考核等级
    private String assessmentLevel;
}
