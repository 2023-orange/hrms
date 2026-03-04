package com.sunten.hrms.pm.dto;

import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * @author batan
 * @since 2020-08-04
 */
@Data
@ToString(callSuper = true)
public class PmEmployeeAwardQueryCriteria extends FndDynamicQueryBaseCriteria {
    private Long employeeId;
    private Set<Long> deptIds;
    private Boolean enabled;
    private Boolean referenceBackup;

    private String awardType; // 奖罚类型，奖励：Reward；扣罚：Fine，空时查所有

//    private String colName;
//    private String symbol;
//    private String value;
}
