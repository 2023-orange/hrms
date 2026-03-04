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
 * 技术爱好临时表
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeHobbyTemp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 技术爱好表id
     */
//    @NotNull
//    private Long headerId;
    @TableField(exist = false)
    private PmEmployeeHobby employeeHobby;

//    /**
//     * 员工id
//     */
//    private Long employeeId;
    @TableField(exist = false)
    private PmEmployee employee;

    /**
     * 技能爱好
     */
    private String hobby;

    /**
     * 级别
     */
    private String levelMyself;

    /**
     * 认证等级
     */
    private String levelMechanism;

    /**
     * 备注
     */
    private String remarks;

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
