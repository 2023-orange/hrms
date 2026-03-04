package com.sunten.hrms.ac.dto;

import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zouyp
 * @since 2023-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PmLeaveApplicationQueryCriteria extends FndDynamicQueryBaseCriteria {
    private String user_name;
    private String reqStatus;
    public void setWorkCard(String workCard) {
        this.user_name = workCard;
    }
}
