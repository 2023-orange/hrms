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
 * @since 2019-12-25
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ToolEmailConfigDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // ID
    private Long id;

    // 收件人
    private String fromUser;

    // 邮件服务器SMTP地址
    private String host;

    // 密码
    private String pass;

    // 端口
    private String port;

    // 发件者用户名
    private String username;

    //是否需要权限验证
    private Boolean needAuth;

    //通过ssl发送
    private Boolean sslEnable;

}
