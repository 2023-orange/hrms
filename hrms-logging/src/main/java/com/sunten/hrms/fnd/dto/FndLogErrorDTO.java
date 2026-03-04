package com.sunten.hrms.fnd.dto;

import com.sunten.hrms.base.BaseDTO;
import lombok.Data;

/**
* @author batan
* @since 2019-5-22
*/
@Data
public class FndLogErrorDTO extends BaseDTO{

    private Long id;

    // 操作用户
    private String username;

    // 描述
    private String description;

    // 方法名
    private String method;

    // 参数
    private String params;

    private String browser;

    // 请求ip
    private String requestIp;

    private String address;


}