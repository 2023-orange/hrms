package com.sunten.hrms.swm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 月度考核表(一个季度生成三条月度)
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmMonthlyQuarterlyAssessment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 考核月度（格式：年.月）
     */
    @NotBlank
    private String assessmentMonth;

    /**
     * 工牌号
     */
    @NotBlank
    private String workCard;

    /**
     * 姓名
     */
    @NotBlank
    private String name;

    /**
     * 考核等级
     */
    @NotBlank
    private String assessmentLevel;

    /**
     * 提交标识（1提交，0未提交）
     */
    @NotNull
    private Boolean submissionIdentificationFlag;

    /**
     * 提交人
     */
    private String submitter;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 区分月度季度
     */
    @NotBlank
    private String assessmentType;

    @NotNull
    private Long seId;

    private String updateByStr; //最后修改人


    private Boolean enabledFlag;

    // 部门
    private String department;
    // 科室
    private String administrativeOffice;

    // 编辑标识
    private Boolean checkFlag = false;

    private BigDecimal assessmentNum;

    //smqa.YS, smqa.S, smqa.A, smqa.B, smqa.C, smqa.D, t2.FZ
    private String year;
    private String YS;
    private String typeS;
    private String typeA;
    private String typeB;
    private String typeC;
    private String typeD;
    private String score;
    /**
     * 是否冻结
     */
    @NotNull
    private Boolean frozenFlag;

    private Long deptId;

}
