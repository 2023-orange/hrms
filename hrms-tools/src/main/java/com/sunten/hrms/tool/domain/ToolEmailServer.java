package com.sunten.hrms.tool.domain;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2020-11-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class ToolEmailServer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 发件人
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
    @NotBlank
    private String username;

    /**
     * 是否需要权限验证
     */
    @NotNull
    private Boolean needAuthFlag;

    /**
     * 通过ssl发送
     */
    @NotNull
    private Boolean sslEnableFlag;

    /**
     * 优先级
     */
    @NotNull
    private Integer priorityLevel;

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;

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

    @NotBlank
    private String serverName;

    private String description;


}
