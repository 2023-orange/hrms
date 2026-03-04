package com.sunten.hrms.fnd.dto;

import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author batan
 * @since 2022-07-26
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndDynamicQueryOperatorGroupDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String remark;

    private Integer sort;

    private Boolean enabledFlag;

    private List<FndDynamicQueryOperatorGroupDetailDTO> dynamicQueryOperatorGroupDetails;

}
