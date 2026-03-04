package com.sunten.hrms.re.dto;

import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author batan
 * @since 2020-08-05
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ReRecruitmentQueryCriteria extends FndDynamicQueryBaseCriteria {

    private Boolean enabled;
    private Boolean originalContractFlag;//是否解除原劳动合同
    // 是否有职业病
    private Boolean occupationalDiseasesFlag;

    // 是否解除原单位协议限制
    private Boolean confidentialityRestrictionsFlag;

    // 健康情况
    private Boolean healthFlag;

    // 是否有亲属在我公司工作
    private Boolean relationshipFlag;
    // 最终骋用标识
    private Boolean recruitmentFlag;

    private String name; // 姓名查询条件


//    private String colName;
//    private String symbol;
//    private String value;

    private Boolean expectInUsedPersonFlag;
}
