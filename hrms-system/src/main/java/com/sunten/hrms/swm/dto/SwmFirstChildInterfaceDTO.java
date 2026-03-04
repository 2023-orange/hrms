package com.sunten.hrms.swm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2021-08-10
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmFirstChildInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 工牌号
    private String workCard;

    // 数据分组id
    private Long groupId;

    // 员工id
    private Long employeeId;

    // 操作码
    private String operationCode;

    // 错误信息
    private String errorMsg;

    // 数据状态
    private String dataStatus;

    // 弹性域
    private String attribute1;

    // 弹性域
    private String attribute2;

    // 弹性域
    private String attribute3;

    // 出生日期
    private LocalDate date;

    // 子女名称
    private String childName;

    // 员工姓名
    private String name;

    private Long id;


}
