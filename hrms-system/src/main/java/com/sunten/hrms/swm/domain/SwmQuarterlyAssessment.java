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
import java.time.LocalDateTime;

/**
 * <p>
 * 季度考核表（一个季度生成一条，主要用作季度考核查询）
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmQuarterlyAssessment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 考核季度（格式：年.季）
     */
    @NotBlank
    private String assessmentQuarter;

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


    @NotNull
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
