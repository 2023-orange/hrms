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
import java.util.List;

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
public class FndSuperQueryTable extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

//    @NotNull
//    private Long groupId;
    private FndSuperQueryGroup group;

    @NotBlank
    private String tableName;

    @NotBlank
    private String tableDescription;

    @NotBlank
    private String tableAbbreviation;

    private String selectArea;

    private String tableArea;

    private String whereArea;

    @NotNull
    private Integer sort;

    @NotNull
    private Boolean enabledFlag;

    private List<FndSuperQueryTableColumn> columns;
}
