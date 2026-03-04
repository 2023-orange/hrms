package com.sunten.hrms.kpi.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.baomidou.mybatisplus.annotation.TableField;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhoujy
 * @since 2023-11-28
 */
@Getter
@Setter
@ToString(callSuper = true)
public class KpiAssessmentIndicatorsMonthDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 主键
    private Long id;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 弹性域4
    private String attribute4;

    // 弹性域5
    private String attribute5;

    // Kpi考核指标ID
    private Long kpiAssessmentIndicatorsId;

    // 一月
    private String January;

    // 二月
    private String February;

    // 三月
    private String March;

    // 四月
    private String April;

    // 五月
    private String May;

    // 六月
    private String June;

    // 七月
    private String July;

    // 八月
    private String August;

    // 九月
    private String September;

    // 十月
    private String October;

    // 十一月
    private String November;

    // 十二月
    private String December;

    // Q1
    private String spring;

    // Q1考核结果
    private String springAssessmentResults;

    // Q1考核得分
    private String springAssessmentScore;

    // Q1修正考核结果
    private String springReviseAssessmentResults;

    // Q1修正得分
    private String springCorrectionScore;

    // Q2
    private String summer;

    // Q2考核结果
    private String summerAssessmentResults;

    // Q2考核得分
    private String summerAssessmentScore;

    // Q2修正考核结果
    private String summerReviseAssessmentResults;

    // Q2修正得分
    private String summerCorrectionScore;

    // Q3
    private String autumn;

    // Q3考核结果
    private String autumnAssessmentResults;

    // Q3考核得分
    private String autumnAssessmentScore;

    // Q3修正考核结果
    private String autumnReviseAssessmentResults;

    // Q3修正得分
    private String autumnCorrectionScore;

    // Q4
    private String winter;

    // Q4考核结果
    private String winterAssessmentResults;

    // Q4考核得分
    private String winterAssessmentScore;

    // Q1得分
    private String springScore;

    // Q1修正
    private String springCorrect;

    // Q2得分
    private String summerScore;

    // Q2修正
    private String summerCorrect;

    // Q3得分
    private String autumnScore;

    // Q3修正
    private String autumnCorrect;

    // Q4得分
    private String winterScore;


}
