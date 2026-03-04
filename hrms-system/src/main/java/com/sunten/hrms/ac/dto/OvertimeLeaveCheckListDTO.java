package com.sunten.hrms.ac.dto;

import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Zouyp
 */
@Getter
@Setter
@ToString(callSuper = true)
public class OvertimeLeaveCheckListDTO extends BaseDTO {
    private String formattedMonth;
    private String nickName;
    private String userName;
    private String deptName;
    private String sectionName;
    private String groupName;
    private String jobName;
    private Float workDayOvertime;
    private Float restDayOvertime;
    private Float TXTS;
    private Float TXXS;
    private Float NJ;
    private Float SJ;
    private Float BJ;
    private Float HJ;
    private Float PCJ;
    private Float SAJ;
    private Float GSJ;
    private Float JHSYJ;
    private Float GJ;
    private Float YEJ;
    private Float HLJ;

}
