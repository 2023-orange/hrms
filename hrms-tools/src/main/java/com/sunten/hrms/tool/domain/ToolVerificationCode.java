package com.sunten.hrms.tool.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author batan
 * @since 2019-12-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ToolVerificationCode extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 验证码
     */
    private String code;

    /**
     * 状态：1有效、0过期
     */
    private Boolean status = true;

    /**
     * 验证码类型：email或者短信
     */
    @NotBlank
    private String type;

    /**
     * 接收邮箱或者手机号码
     */
    @NotBlank
    private String value;

    /**
     * 业务名称：如重置邮箱、重置密码等
     */
    private String scenes;

    public ToolVerificationCode(String code, String scenes, @NotBlank String type, @NotBlank String value) {
        this.code = code;
        this.scenes = scenes;
        this.type = type;
        this.value = value;
    }
}
