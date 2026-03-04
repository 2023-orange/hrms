package com.sunten.hrms.fnd.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 构建前端路由时用到
 * @author batan
 * @since 2019-12-04
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FndMenuVo implements Serializable {

    private String name;

    private String path;

    private Boolean hidden;

    private String redirect;

    private String component;

    private Boolean alwaysShow;

    private FndMenuMetaVo meta;

    private List<FndMenuVo> children;
}
