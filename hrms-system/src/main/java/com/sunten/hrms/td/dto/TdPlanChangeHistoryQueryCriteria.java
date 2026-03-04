package com.sunten.hrms.td.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2021-06-16
 */
@Data
public class TdPlanChangeHistoryQueryCriteria implements Serializable {
    private Long parentId;
    private String changeType;
    private String oaOrder;

}
