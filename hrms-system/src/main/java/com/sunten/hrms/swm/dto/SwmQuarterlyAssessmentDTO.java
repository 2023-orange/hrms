package com.sunten.hrms.swm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;

/**
 * @author liangjw
 * @since 2020-11-24
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmQuarterlyAssessmentDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 考核季度（格式：年.季）
    private String assessmentQuarter;

    // 工牌号
    private String workCard;

    // 姓名
    private String name;

    // 考核等级
    private String assessmentLevel;

    private Long seId;


    private Boolean enabledFlag;


    private String updateByStr; //最后修改人

    // 部门
    private String department;
    // 科室
    private String administrativeOffice;


    private Boolean submissionIdentificationFlag;

    private String submitter;

    private LocalDateTime submitTime;


    // 编辑标识
    private Boolean checkFlag = false;

    private BigDecimal assessmentNum;
}
