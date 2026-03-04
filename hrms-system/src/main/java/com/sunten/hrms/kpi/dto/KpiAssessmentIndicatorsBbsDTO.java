package com.sunten.hrms.kpi.dto;

    import com.baomidou.mybatisplus.annotation.TableName;
    import com.baomidou.mybatisplus.annotation.TableField;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhoujy
 * @since 2023-12-26
 */
@Getter
@Setter
@ToString(callSuper = true)
public class KpiAssessmentIndicatorsBbsDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 主键
    private Long id;

    // 考核指标ID
    private Long kpiAssessmentIndicatorsId;

    // BBS内容
    private String bbsContent;

    // 创建人姓名
    private String createName;

    private Boolean enabledFlag;
}
