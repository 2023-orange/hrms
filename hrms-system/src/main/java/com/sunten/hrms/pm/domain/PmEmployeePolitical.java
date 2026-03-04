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
import java.util.Set;

/**
 * <p>
 * 政治面貌表
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeePolitical extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private PmEmployeePoliticalTemp politicalTemp;
    @TableField(exist = false)
    private String checkFlag;

    /**
     * 修改记录
     */
    @TableField(exist = false)
    Set<PmEmployeePolitical> children;

    @TableField(exist = false)
    private String idKey;
    //修改标记，前台编辑使用
    @TableField(exist = false)
    private Boolean setCheck = false;

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
     * 弹性域1
     */
    private String attribute1;

    /**
     * 弹性域2
     */
    private String attribute2;

    /**
     * 弹性域3
     */
    private String attribute3;

    /**
     * 有效标记默认值
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


}
