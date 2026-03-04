package com.sunten.hrms.fnd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author zhoujy
 * @since 2023-8-9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class FndJobAndDept extends BaseEntity {

    private static final long serialVersionUID = 1L;


    // 工作名称
    private String jobName;

    // 部门名称
    private String deptName;

    // 生产,非生产
    private String jobClass;
}
