package com.sunten.hrms.ac.dto;

import com.sunten.hrms.pm.domain.PmEmployeeJob;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @author ljw
 * @since 2020-09-17
 */
@Data
public class AcEmployeeAttendanceQueryCriteria implements Serializable {
    /**
     *  @author：liangjw
     *  @Date: 2020/9/27 16:39
     *  @Description: 月份，需要处理
     */
    private LocalDate month;
    /**
     *  @author：liangjw
     *  @Date: 2020/9/27 16:40
     *  @Description: 日期从
     */
    private LocalDate dateFrom;

    /**
     *  @author：liangjw
     *  @Date: 2020/9/27 16:40
     *  @Description: 日期至
     */
    private LocalDate dateTo;

    /**
     *  @author：liangjw
     *  @Date: 2020/9/27 16:41
     *  @Description: 部门id
     */
    private Long deptId;

    /**
     *  @author：liangjw
     *  @Date: 2020/9/27 16:41
     *  @Description: 员工id
     */
    private Long employeeId;

    /**
     *  @author：liangjw
     *  @Date: 2020/11/17 9:11
     *  @Description: 工牌号
     */
    private String workCard;

    /**
     *  @author：liangjw
     *  @Date: 2020/9/27 16:42
     *  @Description: enabled
     */
    private Boolean enabled;

    /**
     *  @author：liangjw
     *  @Date: 2020/9/28 8:57
     *  @Description: 节点下级所有部门
     */
    private List<PmEmployeeJob> employeeJObs;

    /**
     *  @author：liangjw
     *  @Date: 2020/10/10 9:21
     *  @Description: 是否早于今天的数据
     */
    private Boolean today;

    /**
     *  @author：liangjw
     *  @Date: 2020/10/10 10:08
     *  @Description: 日期集合
     */
    private List<LocalDate> dates;

    /**
     *  @author：liangjw
     *  @Date: 2020/10/10 10:09
     *  @Description: 人员id集合
     */
    private List<Long> empIds;

    /**
     *  @author：liangjw
     *  @Date: 2020/10/13 11:35
     *  @Description: 部门id集合
     */
    private Set<Long> deptIds;

    private String name;

    private Boolean showType; // 导出报表格式，true: 时间段，false: 工时
}
