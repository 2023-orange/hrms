package com.sunten.hrms.td.domain;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 培训评价分数表
 * </p>
 *
 * @author liangjw
 * @since 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdTrainingEvaluationScore extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 课程内容组成及编排
     */
    @NotNull
    private Float courseContent;

    /**
     * 活动、讨论、练习及案例
     */
    @NotNull
    private Float activityDiscussionExercise;

    /**
     * 讲师对课程内容的理解程度
     */
    @NotNull
    private Float understandingCourse;

    /**
     * 引导学员思考、鼓励参与
     */
    @NotNull
    private Float guideThink;

    /**
     * 各种培训方法运用得当，与内容结合紧密
     */
    @NotNull
    private Float combinedWithContent;

    /**
     * 对现场的把控
     */
    @NotNull
    private Float controlOfScene;

    /**
     * 培训时间安排合理程度
     */
    @NotNull
    private Float reasonableTrainingSchedule;

    /**
     * 课程适合工作需要程度
     */
    @NotNull
    private Float jobNeeds;

    /**
     * 对此课程的吸收与理解程度
     */
    @NotNull
    private Float absorptionUnderstanding;

    /**
     * 达到预期目标的程度
     */
    @NotNull
    private Float goalsAchieved;

    /**
     * 培训实施id
     */
    @NotNull
    private Long planImplementId;

//    @NotNull
    private Float averageScore;

    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    //培训建议或意见
    private String  trainingOpinion;


}
