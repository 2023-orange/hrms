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
 * 培训计划接口表
 * </p>
 *
 * @author liangjw
 * @since 2021-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdPlanInterface extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 培训名称
     */
    @NotBlank
    private String trainingName;

    /**
     * 培训方式
     */
    private String trainingMethod;

    /**
     * 培训内容
     */
    private String trainingContent;

    /**
     * 培训目的
     */
    private String trainingPurpose;

    /**
     * 培训级别
     */
    @NotBlank
    private String trainingLevel;

    /**
     * 专业分类
     */
    private String professionClassify;

    /**
     * 所属部门
     */
    private String dependenceDept;

    /**
     * 主办部门
     */
    private String hostDept;

    /**
     * 讲师
     */
    private String teacher;

    /**
     * 参加人员
     */
    private String employeeDescribes;

    /**
     * 时间
     */
    private String planDate;

    /**
     * 参训人数
     */
    private Integer employeeQuantity;

    /**
     * 预算(元)
     */
    @NotNull
    private BigDecimal planMoney;

    /**
     * 是否线上审批
     */
    @NotNull
    private Boolean onlineFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 有效标记
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

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


    private Long userId;

    private String trainingNo;


}
