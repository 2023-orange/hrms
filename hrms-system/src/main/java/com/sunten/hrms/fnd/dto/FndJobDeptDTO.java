package com.sunten.hrms.fnd.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2020-12-03
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndJobDeptDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long deptId;

    private Long jobId;


}
