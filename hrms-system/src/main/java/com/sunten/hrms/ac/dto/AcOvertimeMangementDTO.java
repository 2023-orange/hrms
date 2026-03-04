package com.sunten.hrms.ac.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zouyp
 * @since 2023-10-16
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AcOvertimeMangementDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 主键ID
    private Long id;

    // 部门id
    private Integer deptId;

    // 总人数
    private Integer totalNumber;

    // 部门人均加班工时
    private Float averageOvertimeHour;

    // 系统限制时数
    private Float systemLimitHour;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    // 状态（0失效，1生效）
    private Integer enabledFlag;


}
