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
import java.util.Set;

/**
 * <p>
 * 工作外职务表
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeePostother extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private PmEmployeePostotherTemp postotherTemp;
    @TableField(exist = false)
    private String checkFlag;

    /**
     * 修改记录
     */
    @TableField(exist = false)
    Set<PmEmployeePostother> children;

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
     * 单位
     */
//    @NotBlank
    private String company;

    /**
     * 职务
     */
//    @NotBlank
    private String post;

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


}
