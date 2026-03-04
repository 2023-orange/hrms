package com.sunten.hrms.td.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author xukai
 * @since 2021-06-18
 */
@Data
public class TdCoursewareQueryCriteria implements Serializable {
    private String jurisdictionSetting; // 课件权限
    private String column; // 列名
    private String symbol; // 比较符
    private String value; // 查询值
    private Boolean adminFlag; // 培训管理员标识，为false时查询公开或者课程内的课件
    private Long employeeId; // 登陆人员人事ID
    private Long userId; // 登陆人员userID

}
