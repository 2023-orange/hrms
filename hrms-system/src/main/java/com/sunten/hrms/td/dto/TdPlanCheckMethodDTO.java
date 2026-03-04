package com.sunten.hrms.td.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2022-03-08
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdPlanCheckMethodDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // td_plan_implement的id
    private Long planImplementId;

    // 考核方式
    private String checkMethod;

    // 评价结果
    private String evaluationResults;

    private Boolean enabledFlag;

    private Long id;


}
