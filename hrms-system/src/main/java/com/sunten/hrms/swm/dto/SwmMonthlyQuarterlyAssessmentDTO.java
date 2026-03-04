package com.sunten.hrms.swm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import javax.validation.constraints.NotNull;
    import java.math.BigDecimal;
    import java.time.LocalDateTime;

/**
 * @author liangjw
 * @since 2020-11-24
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmMonthlyQuarterlyAssessmentDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 考核月度（格式：年.月）
    private String assessmentMonth;

    // 工牌号
    private String workCard;

    // 姓名
    private String name;

    // 考核等级
    private String assessmentLevel;

    // 提交标识（1提交，0未提交）
    private Boolean submissionIdentificationFlag;

    // 提交人
    private String submitter;

    // 提交时间
    private LocalDateTime submitTime;

    // 区分月度季度
    private String assessmentType;

    private Long seId;
    // 最后修改人
    private String updateByStr;

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

    private Boolean frozenFlag;

    private Long deptId;
}
