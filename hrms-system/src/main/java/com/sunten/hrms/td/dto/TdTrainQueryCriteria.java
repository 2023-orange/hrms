package com.sunten.hrms.td.dto;

import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author batan
 * @since 2020-08-04
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TdTrainQueryCriteria extends FndDynamicQueryBaseCriteria {
    private LocalDate trainStartTime;
    private LocalDate trainEndTime;
    //生效标记，此条件应不为空，否则将查询全部
    private Boolean enabled;

//    private String colName;
//    private String symbol;
//    private String value;
}
