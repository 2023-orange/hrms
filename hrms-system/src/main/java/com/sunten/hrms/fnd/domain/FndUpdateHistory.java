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
 * 历史修改表
 * </p>
 *
 * @author batan
 * @since 2020-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class FndUpdateHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 表名
     */
    @NotBlank
    private String tableName;

    /**
     * 列名
     */
    @NotBlank
    private String columnName;

    /**
     * 表id
     */
    @NotNull
    private Long tableId;

    /**
     * 原值
     */
    @NotBlank
    private String oldValue;

    /**
     * 修改值
     */
    @NotBlank
    private String newValue;


}
