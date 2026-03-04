package com.sunten.hrms.pm.domain;

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
import java.time.LocalDate;

/**
 * <p>
 * 员工政治面貌临时表
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeePoliticalTemp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 政治面貌表id
     */
//    @NotNull
//    private Long headerId;
    @TableField(exist = false)
    private PmEmployeePolitical employeePolitical;

    /**
     * 员工id
     */
//    @NotNull
//    private Long employeeId;
    @TableField(exist = false)
    private PmEmployee employee;

    /**
     * 政治面貌
     */
    @NotBlank
    private String political;

    /**
     * 加入时间
     */
    private LocalDate joiningTime;

    /**
     * 性质
     */
    private String nature;

    /**
     * 转正时间
     */
    private LocalDate formalTime;

    /**
     * 职务
     */
    private String post;

    /**
     * 开始时间
     */
    private LocalDate startTime;

    /**
     * 结束时间
     */
    private LocalDate endTime;

    /**
     * 操作标记
     */
    private String instructionsFlag;

    /**
     * 校核标记
     */
    private String checkFlag;

    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private String attribute1;

}
