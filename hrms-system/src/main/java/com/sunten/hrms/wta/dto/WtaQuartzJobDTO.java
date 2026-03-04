package com.sunten.hrms.wta.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2019-12-23
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WtaQuartzJobDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // ID
    private Long id;

    // Spring Bean名称
    private String beanName;

    // cron 表达式
    private String cronExpression;

    // 状态：1暂停、0启用
    private Boolean pause;

    // 任务名称
    private String jobName;

    // 方法名称
    private String methodName;

    // 参数
    private String params;

    // 备注
    private String remark;


}
