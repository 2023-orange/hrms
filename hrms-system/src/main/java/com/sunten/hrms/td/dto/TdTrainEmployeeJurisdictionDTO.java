package com.sunten.hrms.td.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.pm.dto.PmEmployeeDTO;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.util.List;
    import java.util.Set;

/**
 * @author xukai
 * @since 2021-06-23
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdTrainEmployeeJurisdictionDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 员工id
    private Long employeeId;
    private PmEmployeeDTO pmEmployee;
    // 部门id
    private Long deptId;

    // 删除标识
    private Boolean enabledFlag;

    private String attribute3;

    private String attribute1;

    private Long id;

    private String attribute2;

    // 管辖部门集合
    private List<Long> deptIds;

    private List<Long> addList; // 新增部门
    private List<Long> delList; // 失效部门
}
