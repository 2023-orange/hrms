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
public class FndSuperQueryTableDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

//    private Long groupId;
    private FndSuperQueryGroupDTO group;

    private String tableName;

    private String tableDescription;

    private String tableAbbreviation;

    private String selectArea;

    private String tableArea;

    private String whereArea;

    private Integer sort;

    private Boolean enabledFlag;


}
