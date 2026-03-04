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
 * @since 2022-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class FndDynamicQueryOperatorGroupDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;
    //
//    @NotNull
//    private Long operatorGroupId;
    private FndDynamicQueryOperatorGroup dynamicQueryOperatorGroup;

    @NotBlank
    private String label;

    @NotBlank
    private String value;

    @NotNull
    private Integer sort;

    @NotNull
    private Boolean enabledFlag;


}
