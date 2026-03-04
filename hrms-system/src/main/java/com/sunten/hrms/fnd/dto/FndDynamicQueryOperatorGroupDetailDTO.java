package com.sunten.hrms.fnd.dto;

import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2022-07-26
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndDynamicQueryOperatorGroupDetailDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

//    private Long operatorGroupId;

    private FndDynamicQueryOperatorGroupDTO dynamicQueryOperatorGroup;

    private String label;

    private String value;

    private Integer sort;

    private Boolean enabledFlag;


}
