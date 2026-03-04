package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;

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
 * 合同情况表
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeContract extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 员工id
     */
//    @NotNull
//    private Long employeeId;
    @TableField(exist = false)
    private PmEmployee employee;

    /**
     * 合同类别
     */
    @NotBlank
    private String contractType;

    /**
     * 合同开始时间
     */
    @NotNull
    private LocalDateTime startTime;

    /**
     * 合同结束时间
     */
    @NotNull
    private LocalDateTime endTime;

    /**
     * 合同期限
     */
    @NotBlank
    private String contractValidity;

    /**
     * 是否最新合同
     */
    @NotNull
    private Boolean newContractFlag;

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
     * 有效标记默认值
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 合同次数
     */
    @TableField(exist = false)
    private Long quantity;

}
