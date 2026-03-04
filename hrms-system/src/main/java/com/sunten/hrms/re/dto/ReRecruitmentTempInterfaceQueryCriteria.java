package com.sunten.hrms.re.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2021-09-08
 */
@Data
public class ReRecruitmentTempInterfaceQueryCriteria implements Serializable {
    private Long groupId;

    private String dataStatus;

    private String recruitmentQuery;
}
