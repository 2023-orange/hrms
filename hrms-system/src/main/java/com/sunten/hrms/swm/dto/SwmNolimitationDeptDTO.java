package com.sunten.hrms.swm.dto;

    import com.baomidou.mybatisplus.annotation.TableName;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhoujy
 * @since 2023-11-21
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmNolimitationDeptDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    // 不限制评A部门的id
    private Long deptId;

    private Long enabledFlag;

    private String deptName;
}
