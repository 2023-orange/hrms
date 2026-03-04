package com.sunten.hrms.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FndDataScopeVo {
    /**
     * 部门id,查询该部门下所有子节点信息
     */
    private Long deptAllId;

    /**
     * 部门id，只查询本部门一级节点信息
     */
    private Long deptId;

    /**
     * 是否把本人的数据范围当作本级处理
     */
    private Boolean isMySelfAsMyDept;

    /**
     * 是否检查角色数据范围
     */
    private Boolean isCheckRole;

    /**
     * 部门id及其下所有子ID集合,根据部门id获取
     */
    private Set<Long> deptIds;

    /**
     * 员工Id
     */
    private Long employeeId;
}
