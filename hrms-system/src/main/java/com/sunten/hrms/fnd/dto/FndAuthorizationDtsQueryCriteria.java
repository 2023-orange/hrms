package com.sunten.hrms.fnd.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author xukai
 * @since 2021-02-02
 */
@Data
public class FndAuthorizationDtsQueryCriteria implements Serializable {
    private Long authorizationId; // 授权主表ID
}
