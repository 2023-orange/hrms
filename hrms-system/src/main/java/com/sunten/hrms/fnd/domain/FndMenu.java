package com.sunten.hrms.fnd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class FndMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 菜单名称
     */
    @NotBlank
    private String name;

    /**
     * 组件
     */
    private String component;

    /**
     * 上级菜单ID
     */
    @NotNull
    private Long pid;

    /**
     * 排序
     */
    private Long sort = 999L;

    /**
     * 图标
     */
    private String icon;

    /**
     * 链接地址
     */
    private String path;


    private String componentName;

    private String permission;

    private Integer type;

    /**
     * 是否外链
     */
    private Boolean iFrameFlag;

    private Boolean cacheFlag;

    private Boolean hiddenFlag;

    private Boolean blankFlag;

    private Boolean sendTokenFlag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FndMenu menu = (FndMenu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
