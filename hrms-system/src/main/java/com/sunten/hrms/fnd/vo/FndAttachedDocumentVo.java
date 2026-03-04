package com.sunten.hrms.fnd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author batan
 * @since 2020-09-25
 */
@Data
@AllArgsConstructor
public class FndAttachedDocumentVo {
    private String name;
    private String url;
    private Long id;
}
