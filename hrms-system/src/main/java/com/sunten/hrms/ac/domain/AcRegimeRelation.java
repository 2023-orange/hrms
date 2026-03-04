package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 考勤制度排班时间关系表
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcRegimeRelation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 制度id
     */
    @NotNull
    private Long regimeaId;
    /**
     * 制度
     */
    @TableField(exist = false)
    private AcRegime acRegime;

    /**
     * 排班
     */
    // @TableField(exist = false) 数据库中不存在的字段
    @TableField(exist = false)
    private AcRegimeTime acRegimeTime;
    /**
     * 排班 id
     */
    @NotNull
    private Long regimeTimeId;

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

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;


}
