package com.sunten.hrms.ac.dto;

import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author zouyp
 * @since 2023-05-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AcLeaveApplicationQueryCriteria extends FndDynamicQueryBaseCriteria {
    private String user_name;
    private String reqStatus;
    private String reqStatus1;
    private String reqStatus2;
    private String reqStatus3;
    private String approvalResult;
    private LocalDate beginDate;
    private LocalDate endDate;
    private String checkMonth; // 跨月申请查询月
    public void setWorkCard(String workCard) {
        this.user_name = workCard;
    }
}
