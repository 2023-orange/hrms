package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.time.LocalTime;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 考勤异常允许设置表
 * </p>
 *
 * @author liangjw
 * @since 2020-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcSetUp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 异常间隔日期
     */
    @NotNull
    private Integer interval;

    /**
     * 允许时间
     */
    @NotNull
    private LocalTime runTime;

    /**
     * 是否0点阶段
     */
    @NotNull
    private Boolean stageFlag;

    private BigDecimal calculation;

    private String attribute1;

    private String attribute2;

    private String attribute3;


}
