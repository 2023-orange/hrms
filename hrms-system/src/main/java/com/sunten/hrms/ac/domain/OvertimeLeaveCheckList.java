package com.sunten.hrms.ac.domain;

import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Zouyp
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class OvertimeLeaveCheckList extends BaseEntity {
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
    private Float CJ;
    private Float PCJ;
    private Float SAJ;
    private Float GSJ;
    private Float JHSYJ;
    private Float GJ;
    private Float YEJ;
    private Float HLJ;
    private Float syjbgs;

}
