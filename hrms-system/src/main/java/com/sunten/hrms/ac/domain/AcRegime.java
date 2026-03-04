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
import java.util.List;

/**
 * <p>
 * 考勤制度表
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcRegime extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 考勤制度名称
     */
    @NotBlank
    private String regimeName;

    /**
     * 考勤制度代号
     */
    private String regimeCode;

    /**
     * 备注
     */
    private String remake;

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

    private List<AcRegimeRelation> acRegimeRelations;

}
