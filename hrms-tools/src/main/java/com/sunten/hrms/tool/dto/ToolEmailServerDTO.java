package com.sunten.hrms.tool.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2020-11-02
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ToolEmailServerDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // ID
    private Long id;

    // 发件人
    private String fromUser;

    // 邮件服务器SMTP地址
    private String host;

    // 密码
    private String pass;

    // 端口
    private String port;

    // 发件者用户名
    private String username;

    // 是否需要权限验证
    private Boolean needAuthFlag;

    // 通过ssl发送
    private Boolean sslEnableFlag;

    // 优先级
    private Integer priorityLevel;

    // 有效标记
    private Boolean enabledFlag;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 弹性域4
    private String attribute4;

    // 弹性域5
    private String attribute5;

    private String serverName;

    private String description;


}
