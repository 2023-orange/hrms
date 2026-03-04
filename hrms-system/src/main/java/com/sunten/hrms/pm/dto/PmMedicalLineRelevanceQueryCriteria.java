package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author xukai
 * @since 2021-04-20
 */
@Data
public class PmMedicalLineRelevanceQueryCriteria implements Serializable {
    private Long medicalLineId; // 体检信息子表ID
}
