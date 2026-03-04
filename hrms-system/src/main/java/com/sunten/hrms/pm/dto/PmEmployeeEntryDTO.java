package com.sunten.hrms.pm.dto;

    import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author batan
 * @since 2020-08-04
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmEmployeeEntryDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 员工id
//    private Long employeeId;
    private PmEmployeeDTO employee;

    // 入职时间
    private LocalDate entryTime;

    // 入职录用方式
    private String entryMode;

    // 是否有人事档案
    private Boolean entryArchivesFlag;

    // 档案不详
    private String archivesUnknown;

    // 是否有犯罪记录
    private Boolean crimeFlag;

    // 试用期（月）
    private Integer probation;

    // 转正时间
    private LocalDate formalTime;

    // 介绍信工资
    private String introductionWages;

    // 介绍信情况
    private String introductionSituation;

    // 档案所在地
    private String archivesAddress;

    // 过往工龄
    private Integer workedYears;

    // 备注
    private String remarks;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 有效标记默认值
    private Boolean enabledFlag;

    private Long id;
    //试用期结束时间
    private LocalDate probationEndTime;

    private String assessFlag; // 是否生成过试用考核，Y是，N否

}
