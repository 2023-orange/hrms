package com.sunten.hrms.td.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author xukai
 * @since 2021-06-23
 */
@Data
public class TdTrainEmployeeJurisdictionQueryCriteria implements Serializable {
    private String column; // 列名
    private String symbol; // 比较符
    private String value; // 查询值
    private Boolean enabledFlag; // 生效标识
}
