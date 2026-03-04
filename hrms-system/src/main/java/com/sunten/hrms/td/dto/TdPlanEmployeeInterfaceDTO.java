package com.sunten.hrms.td.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2021-09-24
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdPlanEmployeeInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 数据分组id
    private Long groupId;

    // 操作码
    private String operationCode;

    // 错误信息
    private String errorMsg;

    // 数据状态
    private String dataStatus;

    // 姓名
    private String name;

    // 工牌号
    private String workCard;

    private Long id;


    private Long employeeId;


}
