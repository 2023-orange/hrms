package com.sunten.hrms.td.domain;

import java.math.BigDecimal;
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
 * 培训考核情况
 * </p>
 *
 * @author liangjw
 * @since 2022-03-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdPlanInspectionSituation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 实施id
     */
    @NotNull
    private Long planImplementId;

    /**
     * 考核的总人数
     */
    @NotNull
    private Integer peopleAssessedTotal;

    /**
     * 一次合格人数
     */
    @NotNull
    private Integer oneTimePassTotal;

    /**
     * 一次合格率
     */
    @NotNull
    private BigDecimal primaryPassRate;

    /**
     * 补考人数
     */
    private Integer makeExaminationNumber;

    /**
     * 补考合格率
     */
    private BigDecimal makePassRate;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    @NotNull
    private Boolean enabledFlag;

    @NotBlank
    private String checkMethod;


}
