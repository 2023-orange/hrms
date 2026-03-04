package com.sunten.hrms.swm.dto;

import com.sunten.hrms.fnd.dto.AdvancedCriteriaQuery;
import com.sunten.hrms.swm.domain.SwmBonusPaymentInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SwmBonusPaymentQueryCriteria extends AdvancedCriteriaQuery implements Serializable {
    private Long bonusId;

    private Long employeeId;

}
