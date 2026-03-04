package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 厂车迟到时间记录表
 * </p>
 *
 * @author xukai
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcBeLateDate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 迟到日期
     */
    @NotNull
    private LocalDate beLateDate;

    /**
     * 迟到延迟时间，默认9:00
     */
    @NotNull
    private LocalTime beLateTime;

    /**
     * 年份
     */
    private String beLateYear;

    /**
     * 月份
     */
    private Integer beLateMonth;

    /**
     * 弹性域1
     */
    private String attribute1;

    /**
     * 弹性域2
     */
    private String attribute2;

    /**
     * 弹性域3
     */
    private String attribute3;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;
    /**
     * 生效标记
     */
    private Boolean enabledFlag;

}
