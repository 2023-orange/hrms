package com.sunten.hrms.fnd.dto;

import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2022-08-12
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndSuperQueryTableColumnDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    //    private Long groupId;
    private FndSuperQueryGroupDTO group;
//    private Long tableId;
    private FndSuperQueryTableDTO table;

    private String tableAbbreviation;

    private String columnName;

    private String columnDescription;

    private Integer convertStyle;

    private String alias;

    private Integer sort;

    private Boolean enabledFlag;


}
