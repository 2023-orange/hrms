package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author xukai
 * @since 2021-04-19
 */
@Data
public class PmMedicalProjectQueryCriteria implements Serializable {
    private String projectName; // 项目名称
}
