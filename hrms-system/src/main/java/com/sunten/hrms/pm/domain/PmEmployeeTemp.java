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

/**
 * <p>
 * 人员临时表
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeTemp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 员工id
     */
//    @NotNull
//    private Long employeeId;

    @TableField(exist = false)
    private PmEmployee employee;

    /**
     * 身高
     */
    @NotNull
    private Double height;

    /**
     * 体重
     */
    @NotNull
    private Double weight;

    /**
     * 婚姻状态
     */
    @NotBlank
    private String maritalStatus;

    /**
     * 户口性质
     */
    @NotBlank
    private String householdCharacter;

    /**
     * 现在住址
     */
    @NotBlank
    private String address;

    /**
     * 现住邮编
     */
    private String zipcode;

    /**
     * 户口地址
     */
    @NotBlank
    private String householdAddress;

    /**
     * 户口邮编
     */
    private String householdZipcode;

    /**
     * 是否集体户口
     */
    @NotNull
    private Boolean collectiveHouseholdFlag;

    /**
     * 集体户口所在地
     */
    private String collectiveAddress;

    /**
     * 籍贯
     */
    @NotBlank
    private String nativePlace;

    /**
     * 操作标记
     */
    private String instructionsFlag;

    /**
     * 校核标记
     */
    @NotBlank
    private String checkFlag;

    /**
     * 备注
     */
    private String remarks;

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
     * 弹性域4
     */
    private String attribute4;

    /**
     * 弹性域5
     */
    private String attribute5;

    /**
     * 有效标记默认值
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


}
