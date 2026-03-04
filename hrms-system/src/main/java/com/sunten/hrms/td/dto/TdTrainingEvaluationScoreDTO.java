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
 * @since 2022-03-10
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdTrainingEvaluationScoreDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 课程内容组成及编排
    private Float courseContent;

    // 活动、讨论、练习及案例
    private Float activityDiscussionExercise;

    // 讲师对课程内容的理解程度
    private Float understandingCourse;

    // 引导学员思考、鼓励参与
    private Float guideThink;

    // 各种培训方法运用得当，与内容结合紧密
    private Float combinedWithContent;

    // 对现场的把控
    private Float controlOfScene;

    // 培训时间安排合理程度
    private Float reasonableTrainingSchedule;

    // 课程适合工作需要程度
    private Float jobNeeds;

    // 对此课程的吸收与理解程度
    private Float absorptionUnderstanding;

    // 达到预期目标的程度
    private Float goalsAchieved;

    // 培训实施id
    private Long planImplementId;

    private Float averageScore;

    private Boolean enabledFlag;

    private Long id;

    //培训建议或意见
    private String  trainingOpinion;

}
