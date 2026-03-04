package com.sunten.hrms.pm.dto;

import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import com.sunten.hrms.pm.domain.PmEmployeeJob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Set;

/**
 * @author batan
 * @since 2020-08-04
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PmEmployeeQueryCriteria extends FndDynamicQueryBaseCriteria {

    /**
     * 部门id,查询该部门下所有子节点信息
     */
    private Long deptAllId;

    /**
     * 部门id，只查询本部门下的一级节点信息
     */
    private Long deptId;

    /**
     * 部门id及其下所有子ID集合,根据部门id获取
     */
    private Set<Long> deptIds;

    private Long employeeId;

    /**
     * 员工岗位部门关系
     */
    private List<PmEmployeeJob> employeeJObs;
    /**
     * 有效标记默认值
     */
    private Boolean enabledFlag;
    /**
     * 离职标记
     */
    private Boolean leaveFlag;
    /**
     * 是否考勤
     */
    private Boolean workAttendanceFlag;
    /**
     * 是否排班
     */
    private Boolean workshopAttendanceFlag;
//    /**
//     * 列名
//     */
//    private String colName;
//    /**
//     * 符号
//     */
//    private String symbol;
//    /**
//     * 输入值
//     */
//    private String value;

    /*姓名，工牌*/
    private String nameAndWork;

    /**
     * 工号
     */
    private String workCard;
    //身份证
    private String idNumber;

    /**
     * 姓名
     */
    private String name;

    private String dataType;
//
//    // 解析后的高级查询内容
//    private List<AdvancedQuery> advancedQuerys;
//    // 前台传过来时的高级查询内容的JSON串
//    private String advancedQuerysStr;
}
