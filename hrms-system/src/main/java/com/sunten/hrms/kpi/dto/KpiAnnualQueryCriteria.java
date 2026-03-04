package com.sunten.hrms.kpi.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author zhoujy
 * @since 2023-11-27
 */
@Data
public class KpiAnnualQueryCriteria implements Serializable {

    private String year;

}
