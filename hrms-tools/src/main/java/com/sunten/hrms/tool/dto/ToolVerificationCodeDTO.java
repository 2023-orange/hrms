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
public class ToolVerificationCodeDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // ID
    private Long id;

    // 验证码
    private String code;

    // 状态：1有效、0过期
    private Boolean status;

    // 验证码类型：email或者短信
    private String type;

    // 接收邮箱或者手机号码
    private String value;

    // 业务名称：如重置邮箱、重置密码等
    private String scenes;


}
