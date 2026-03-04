package com.sunten.hrms.tool.domain;

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
public class ToolEmailConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 收件人
     */
    @NotBlank
    private String fromUser;

    /**
     * 邮件服务器SMTP地址
     */
    @NotBlank
    private String host;

    /**
     * 密码
     */
    @NotBlank
    private String pass;

    /**
     * 端口
     */
    @NotBlank
    private String port;

    /**
     * 发件者用户名
     */
    private String username;

    /**
     * 是否需要权限验证
     */
    @TableField(value="is_need_auth")
    @NotNull
    private Boolean needAuth;

    /**
     * 通过ssl发送
     */
    @TableField(value="is_ssl_enable")
    @NotNull
    private Boolean sslEnable;
}
