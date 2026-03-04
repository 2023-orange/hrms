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
public class WtaQuartzLogDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String beanName;

    private String cronExpression;

    private String exceptionDetail;

    private Boolean success;

    private String jobName;

    private String methodName;

    private String params;

    private Long time;


}
