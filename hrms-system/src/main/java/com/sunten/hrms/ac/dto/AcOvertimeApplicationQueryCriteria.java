package com.sunten.hrms.ac.dto;

import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author zouyp
 * @since 2023-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AcOvertimeApplicationQueryCriteria extends FndDynamicQueryBaseCriteria {
    private String user_name;
    private String reqStatus;
    private String reqStatus1;
    private String reqStatus2;
    private String reqStatus3;
    private String approvalResult;
    private LocalDate beginDate;
    private LocalDate endDate;
    public void setWorkCard(String workCard) {
        this.user_name = workCard;
    }
}
