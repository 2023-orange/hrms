package com.sunten.hrms.pm.dto;


import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

/**
 * @author batan
 * @since 2020-08-04
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmEmployeeWorkhistoryDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 员工id
//    private Long employeeId;
    private PmEmployeeDTO employee;

    Set<PmEmployeeWorkhistoryDTO> children;
    //修改标记，前台使用
    private Boolean setCheck = false;

    private String idKey;

    private String checkFlag;
    // 单位
    private String company;

    // 职务
    private String post;

    // 开始时间
    private LocalDate startTime;

    // 结束时间
    private LocalDate endTime;

    // 联系电话
    private String tele;

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


}
