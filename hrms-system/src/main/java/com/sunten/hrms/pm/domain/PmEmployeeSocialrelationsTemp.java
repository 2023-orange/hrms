package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 社会关系临时表
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeSocialrelationsTemp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 社会关系表id
     */
//    @NotNull
//    private Long headerId;
    @TableField(exist = false)
    private PmEmployeeSocialrelations employeeSocialrelations;

    /**
     * 员工id
     */
//    private Long employeeId;
    @TableField(exist = false)
    private PmEmployee employee;

    /**
     * 姓名
     */
    private String name;

    /**
     * 关系
     */
    private String relationship;

    /**
     * 单位
     */
    private String company;

    /**
     * 职务
     */
    private String post;

    /**
     * 电话
     */
    private String tele;

    /**
     * 是否在厂工作
     */
    private Boolean inFactoryFlag;

    /**
     * 操作标记
     */
    private String instructionsFlag;

    /**
     * 校核标记
     */
    private String checkFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    @NotNull
    private Boolean enabledFlag;


}
