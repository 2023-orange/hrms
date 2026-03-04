package com.sunten.hrms.td.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 培训员权限表
 * </p>
 *
 * @author xukai
 * @since 2021-06-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdTrainEmployeeJurisdiction extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 员工id
     */
    @NotNull
    private Long employeeId;

    private PmEmployee pmEmployee;

    /**
     * 部门id
     */
    @NotNull
    private Long deptId;

    /**
     * 删除标识
     */
    @NotNull
    private Boolean enabledFlag;

    private String attribute3;

    private String attribute1;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private String attribute2;
    // 管辖部门集合
    private List<Long> deptIds;

    private List<Long> addList; // 新增部门
    private List<Long> delList; // 失效部门
}
