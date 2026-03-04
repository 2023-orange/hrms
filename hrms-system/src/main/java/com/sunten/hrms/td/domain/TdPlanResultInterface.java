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
 * 培训结果接口表
 * </p>
 *
 * @author liangjw
 * @since 2021-06-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdPlanResultInterface extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 工牌号
     */
    @NotBlank
    private String workCard;

    /**
     * 培训计划id
     */
    @NotNull
    private Long planId;

    /**
     * 员工出勤情况
     */
    private String attendance;

    /**
     * 培训时长
     */
    private Float duration;

    /**
     * 成绩
     */
    private Float grade;

    /**
     * 满意度
     */
    private String evaluate;

    /**
     * 是否签订培训协议书
     */
    @NotNull
    private Boolean needFlag;

    /**
     * 生效标记
     */
    @NotNull
    private Boolean enabledFlag;

    /**
     * 操作码
     */
    @NotBlank
    private String operationCode;

    /**
     * 错误信息
     */
    @NotBlank
    private String errorMsg;

    /**
     * 数据状态
     */
    @NotBlank
    private String dataStatus;

    /**
     * 数组分组ID
     */
    @NotNull
    private Long groupId;

    /**
     * 姓名
     */
    @NotBlank
    private String name;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private Double scoreLine;

    private Boolean matchImplementFlag;

    private String remarks;

    private String checkMethod;


}
