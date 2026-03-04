package com.sunten.hrms.fnd.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2024-06-06
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndServerInformationDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    // 服务名
    private String serverName;

    // 描述
    private String description;

    // 前端uri
    private String frontendUri;

    // 后端uri
    private String backendUri;

    // 数据库uri
    private String databaseUri;

    // 有效标记默认值
    private Boolean enabledFlag;

    // 备注
    private String remarks;

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


}
