package com.sunten.hrms.wta.domain;

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
 * 
 * </p>
 *
 * @author batan
 * @since 2019-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class WtaQuartzJob extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public static final String JOB_KEY = "JOB_KEY";
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * Spring Bean名称
     */
    @NotBlank
    private String beanName;

    /**
     * cron 表达式
     */
    @NotBlank
    private String cronExpression;

    /**
     * 状态：1暂停、0启用
     */
    @TableField(value="is_pause")
    private Boolean pause = false;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 方法名称
     */
    @NotBlank
    private String methodName;

    /**
     * 参数
     */
    private String params;

    /**
     * 备注
     */
    @NotBlank
    private String remark;


}
