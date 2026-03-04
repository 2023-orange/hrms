package com.sunten.hrms.td.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 * 培训结果记录
 * </p>
 *
 * @author liangjw
 * @since 2021-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdPlanResult extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 员工id
     */
    @NotNull
    private Long employeeId;

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

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    @TableField(exist = false)
    private String name; // 姓名

    @TableField(exist = false)
    private String workCard; // 工牌号

    @TableField(exist = false)
    private  String deptName; // 部门

    @TableField(exist = false)
    private String department; // 科室

    @TableField(exist = false)
    private String  team; // 班组

    @TableField(exist = false)
    private Boolean editFlag;

    @TableField(exist = false)
    private String idNumber;
    @TableField(exist = false)
    private Date contractEndDate;
    @TableField(exist = false)
    private Date contractStartDate;
    // 是否有培训协议的标记
    private Boolean haveAgreementFlag;

    private Double scoreLine;

    private String remarks;

    private Boolean matchImplementFlag;

    private String checkMethod;



}
