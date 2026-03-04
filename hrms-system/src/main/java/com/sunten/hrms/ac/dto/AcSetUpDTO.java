package com.sunten.hrms.ac.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;

    import java.math.BigDecimal;
    import java.time.LocalTime;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2020-10-23
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AcSetUpDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 异常间隔日期
    private Integer interval;

    // 允许时间
    private LocalTime runTime;

    // 是否0点阶段
    private Boolean stageFlag;

    private BigDecimal calculation;

    private String attribute1;

    private String attribute2;

    private String attribute3;


}
