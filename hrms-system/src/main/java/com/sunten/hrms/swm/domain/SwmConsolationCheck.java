package com.sunten.hrms.swm.domain;

import lombok.Data;

@Data
public class SwmConsolationCheck {
    // 系统中已存在诞辰的标记
    private Boolean haveBornFlag;
    // 系统中有配偶存在诞辰的标记
    private Boolean haveHalfBornFlag;
    // 系统中有结婚的标记
    private Boolean haveMarriedFlag;
    //  系统中有伴侣提交结婚的标记
    private Boolean haveHalfMarriedFlag;
    // 系统中有子女幼托的标记
    private Boolean haveChildFlag;
    // 系统中有伴侣子女幼托的标记
    private Boolean haveHalfChildFlag;
    // 系统中有丧事的标记
    private Boolean haveDeadFlag;
}
