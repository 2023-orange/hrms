package com.sunten.hrms.fnd.dto;

import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author batan
 * @since 2019-12-19
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndDictDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    // 字典名称
    private String name;

    // 描述
    private String remark;

    private List<FndDictDetailDTO> dictDetails;

}
