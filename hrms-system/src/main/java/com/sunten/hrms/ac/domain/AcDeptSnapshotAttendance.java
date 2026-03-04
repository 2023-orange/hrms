package com.sunten.hrms.ac.domain;

import com.sunten.hrms.fnd.domain.FndDeptSnapshot;
import lombok.Data;

@Data
public class AcDeptSnapshotAttendance {
    private AcDeptAttendance acDeptAttendance;
    private FndDeptSnapshot fndDeptSnapshot;
}
