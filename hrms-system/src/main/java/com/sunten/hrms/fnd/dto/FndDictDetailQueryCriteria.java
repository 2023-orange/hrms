package com.sunten.hrms.fnd.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author batan
 * @since 2019-12-19
 */
@Data
public class FndDictDetailQueryCriteria implements Serializable {

    private String label;

    private String dictName;

    private String value;
}
