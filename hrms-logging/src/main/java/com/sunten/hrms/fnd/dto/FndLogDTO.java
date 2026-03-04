package com.sunten.hrms.fnd.dto;

import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2019-12-23
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndLogDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String description;

    private String exceptionDetail;

    private String logType;

    private String method;

    private String params;

    private String requestIp;

    private Long time;

    private String username;

    private String address;

    private String browser;


}
