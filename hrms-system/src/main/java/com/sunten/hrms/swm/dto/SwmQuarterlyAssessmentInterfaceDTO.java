package com.sunten.hrms.swm.dto;

    import java.math.BigDecimal;
    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2022-05-13
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmQuarterlyAssessmentInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 数据分组id
    private Long groupId;

    // 工牌号
    private String workCard;

    // 员工姓名
    private String employeeName;

    // 操作码
    private String operationCode;

    // 错误信息
    private String errorMsg;

    // 数据状态
    private String dataStatus;

    // 考核季度
    private String assessmentQuarter;

    // 考核系数（负责新数据的写入）
    private BigDecimal assessmentNum;

    // 薪酬人员id
    private Long seId;


}
