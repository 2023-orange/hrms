package com.sunten.hrms.pm.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @atuthor xukai
 * @date 2020/8/13 9:59
 */
@Data
public class PmEmployeeLikeQueryCriteria implements Serializable {
    //名称
    private String name;
    //工牌
    private String workCard;
    //身份证
    private String idNumber;
    /**
     * 工牌、姓名、身份证匹配字符串
     */
    private String identityNmae;
    private Set<Long> deptIds;
    private Long employeeId;
}
