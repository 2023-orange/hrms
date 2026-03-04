package com.sunten.hrms.pm.domain;

import lombok.Data;


@Data
public class PmUsedAnnualLeave {
    private Integer year;

    private Float usedAnnualLeave;

    private String workCard;
}
