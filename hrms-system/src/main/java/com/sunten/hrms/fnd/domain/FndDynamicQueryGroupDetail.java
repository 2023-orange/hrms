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
 * @since 2022-07-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class FndDynamicQueryGroupDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

//    @NotNull
//    private Long groupId;
    private FndDynamicQueryGroup dynamicQueryGroup;

    @NotBlank
    private String label;

    private String queryTable;

    private String field;

    @NotBlank
    private String fieldType;

    @NotBlank
    private String fieldTypeDesc;

    @NotBlank
    private String jdbcType;

//    private Long operatorGroupId;
    private FndDynamicQueryOperatorGroup dynamicQueryOperatorGroup;

    @NotBlank
    private String component;

    private String componentAttributes;

    @NotBlank
    private String listType;

    private String listAttributes;

    private String regularExpression;

    private String specialSql;

    @NotNull
    private Integer sort;

    private Boolean enabledFlag;


}
