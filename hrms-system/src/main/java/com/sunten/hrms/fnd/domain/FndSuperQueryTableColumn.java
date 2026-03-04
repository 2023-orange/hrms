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
 *
 * </p>
 *
 * @author batan
 * @since 2022-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class FndSuperQueryTableColumn extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    //    @NotNull
//    private Long groupId;
    private FndSuperQueryGroup group;
    //    @NotNull
//    private Long tableId;
    private FndSuperQueryTable table;

    private String tableAbbreviation;

    @NotBlank
    private String columnName;

    @NotBlank
    private String columnDescription;

    private Integer convertStyle;

    private String alias;

    @NotNull
    private Integer sort;

    @NotNull
    private Boolean enabledFlag;


}
