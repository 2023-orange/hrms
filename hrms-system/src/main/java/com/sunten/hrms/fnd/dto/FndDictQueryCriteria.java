package com.sunten.hrms.fnd.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author batan
 * @since 2019-12-19
 */
@Data
public class FndDictQueryCriteria implements Serializable {

    // 多字段模糊
    private String blurry;
}
