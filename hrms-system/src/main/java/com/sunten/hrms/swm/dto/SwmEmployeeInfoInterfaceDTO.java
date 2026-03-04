package com.sunten.hrms.swm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhoujy
 * @since 2023-03-23
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmEmployeeInfoInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Double id;

    private Long employeeId;

    private String workCard;

    private String name;

    // 数据分组id
    private Long groupId;

    // 操作码
    private String operationCode;

    // 错误信息
    private String errorMsg;

    // 数据状态
    private String dataStatus;

    // 银行账号
    private String bankAccount;

    // 银行名称
    private String bankName;

    // 部门
    private String department;

    // 科室
    private String administrativeOffice;

    // 班组
    private String team;

    // 岗位
    private String station;

    // 成本中心号
    private String costCenterNum;

    // 成本中心名称
    private String costCenter;

    // 服务部门
    private String serviceDepartment;


}
