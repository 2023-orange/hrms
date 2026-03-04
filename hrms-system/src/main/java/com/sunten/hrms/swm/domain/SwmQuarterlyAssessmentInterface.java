package com.sunten.hrms.swm.domain;

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
 * 
 * </p>
 *
 * @author liangjw
 * @since 2022-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmQuarterlyAssessmentInterface extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 数据分组id
     */
    @NotNull
    private Long groupId;

    /**
     * 工牌号
     */
    @NotBlank
    private String workCard;

    /**
     * 员工姓名
     */
    @NotBlank
    private String employeeName;

    /**
     * 操作码
     */
    @NotBlank
    private String operationCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 数据状态
     */
    @NotBlank
    private String dataStatus;

    /**
     * 考核季度
     */
    @NotBlank
    private String assessmentQuarter;

    /**
     * 考核系数（负责新数据的写入）
     */
    private BigDecimal assessmentNum;

    /**
     * 薪酬人员id
     */
    @NotNull
    private Long seId;


}
