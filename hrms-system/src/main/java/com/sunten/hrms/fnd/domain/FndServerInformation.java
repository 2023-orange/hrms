package com.sunten.hrms.fnd.domain;

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
 * 服务器信息表
 * </p>
 *
 * @author batan
 * @since 2024-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class FndServerInformation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 服务名
     */
    @NotBlank
    private String serverName;

    /**
     * 描述
     */
    @NotBlank
    private String description;

    /**
     * 前端uri
     */
    @NotBlank
    private String frontendUri;

    /**
     * 后端uri
     */
    @NotBlank
    private String backendUri;

    /**
     * 数据库uri
     */
    @NotBlank
    private String databaseUri;

    /**
     * 有效标记默认值
     */
    @NotNull
    private Boolean enabledFlag;

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
     * 弹性域4
     */
    private String attribute4;

    /**
     * 弹性域5
     */
    private String attribute5;


}
