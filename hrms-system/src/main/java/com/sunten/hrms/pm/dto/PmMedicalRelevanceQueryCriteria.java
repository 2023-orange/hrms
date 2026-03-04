package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author xukai
 * @since 2021-04-19
 */
@Data
public class PmMedicalRelevanceQueryCriteria implements Serializable {
    private String projectType; // 体检项目类型：上岗前体检项目、在岗期间体检项目、离岗体检项目
    private Long medicalJobId; // 岗位体检项主表ID
}
