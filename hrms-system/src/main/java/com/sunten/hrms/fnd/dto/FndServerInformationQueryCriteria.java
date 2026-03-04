package com.sunten.hrms.fnd.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author batan
 * @since 2024-06-06
 */
@Data
public class FndServerInformationQueryCriteria implements Serializable {

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
    private Boolean enabled;

}
