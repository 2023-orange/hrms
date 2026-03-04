package com.sunten.hrms.td.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class TeacherVo {
    private Long id; // 讲师的人员id

    private String name; // 讲师名称
}
