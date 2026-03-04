package com.sunten.hrms.ac.dto;

import com.sunten.hrms.base.BaseDTO;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @atuthor xukai
 * @date 2020/10/16 15:49
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AcOvertimeDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;
    private Long id;// id
    private String workCode; // 工牌
    private String nickName; // 名称
    private String deptName; // 部门
    private String officeName; // 科室
    private String nowDate; // 当前日期
    private Double quantity; // 调休天数
    private Double restNum; // 休息日加班
    private Double workNum; // 工作日加班
}
