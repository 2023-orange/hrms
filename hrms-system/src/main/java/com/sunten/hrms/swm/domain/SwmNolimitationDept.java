package com.sunten.hrms.swm.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("swm_noLimitation_dept")
public class SwmNolimitationDept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long enabledFlag;

    private String deptName;

    private String department;
}
