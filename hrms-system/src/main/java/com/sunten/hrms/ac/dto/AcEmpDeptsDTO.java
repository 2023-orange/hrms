package com.sunten.hrms.ac.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.fasterxml.jackson.annotation.JsonInclude;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.util.List;

/**
 * @author liangjw
 * @since 2020-12-09
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AcEmpDeptsDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 对应fnd_role 目前只考虑资料员
    private Long roleId;

    // 对应人事主表id
    private Long employeeId;

    // 部门id
    private Long deptId;

    // 有效标识
    private Boolean enabledFlag;

    private Long id;

    private String deptName;

    private Long parentId;

    private String dataType;


//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    private List<AcE> children;

    public String getLabel() {
        return deptName;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<AcEmpDeptsDTO> children;




}
