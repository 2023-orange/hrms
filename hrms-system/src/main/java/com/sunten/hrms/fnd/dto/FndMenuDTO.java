package com.sunten.hrms.fnd.dto;

import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author batan
 * @since 2019-12-19
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndMenuDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // ID
    private Long id;

    // 菜单名称
    private String name;

    // 组件
    private String component;

    // 上级菜单ID
    private Long pid;

    // 排序
    private Long sort;

    // 图标
    private String icon;

    // 链接地址
    private String path;


    private String componentName;

    private String permission;

    private Integer type;

    // 是否外链
    private Boolean iFrameFlag;

    private Boolean cacheFlag;

    private Boolean hiddenFlag;

    private Boolean blankFlag;

    private Boolean sendTokenFlag;

    private List<FndMenuDTO> children;

}
