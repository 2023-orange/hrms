package com.sunten.hrms.fnd.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2020-10-26
 */
@Data
public class FndInterfaceOperationRecordQueryCriteria implements Serializable {

    private String operationValue; // 操作值

}
