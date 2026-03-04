package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @author xukai
 * @since 2020-08-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeFamilyTemp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 员工id
     */
//    @NotNull
//    private Long employeeId;
    @TableField(exist = false)
    private PmEmployee employee;

    /**
     * 姓名
     */
//    @NotBlank
    private String name;

    /**
     * 关系
     */
//    @NotBlank
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
//    @NotBlank
    private String tele;

    /**
     * 性别
     */
    private String gender;

    /**
     * 出生日期
     */
    private LocalDate birthday;

    /**
     * 手机
     */
    private String mobilePhone;

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

    /**
     * 操作标记，A添加，U修改
     */
    private String instructionsFlag;

    /**
     * 校核标记，D待校核，Y通过，N不通过
     */
    @NotBlank
    private String checkFlag;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 原家庭情况表ID
     */
//    @NotNull
//    private Integer familyId;
    @TableField(exist = false)
    private PmEmployeeFamily employeeFamily;

    @TableId(value = "id", type = IdType.NONE)
    @NotNull
    private Long id;


}
