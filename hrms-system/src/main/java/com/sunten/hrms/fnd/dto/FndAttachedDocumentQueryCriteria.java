package com.sunten.hrms.fnd.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author batan
 * @since 2020-09-25
 */
@Data
public class FndAttachedDocumentQueryCriteria implements Serializable {
    private String source;
    private Long sourceId;
    private String type;
    private Long id;
    private String realName;
    private String path;
    private boolean enabledFlag = true;
}
