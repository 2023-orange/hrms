package com.sunten.hrms.ac.vo;

import lombok.Data;
import lombok.ToString;

import java.time.LocalTime;

@Data
@ToString(callSuper = true)
public class AcRegimeTimeVo {
    private LocalTime timeFrom;
    private LocalTime timeTo;
    private Boolean extendTimeFlag;
}
