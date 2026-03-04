package com.sunten.hrms.td.dto;

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
 * @since 2021-05-23
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdPlanInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 培训名称
    private String trainingName;

    // 培训方式
    private String trainingMethod;

    // 培训内容
    private String trainingContent;

    // 培训目的
    private String trainingPurpose;

    // 培训级别
    private String trainingLevel;

    // 专业分类
    private String professionClassify;

    // 所属部门
    private String dependenceDept;

    // 主办部门
    private String hostDept;

    // 讲师
    private String teacher;

    // 参加人员
    private String employeeDescribes;

    // 时间
    private String planDate;

    // 参训人数
    private Integer employeeQuantity;

    // 预算(元)
    private BigDecimal planMoney;

    // 是否线上审批
    private Boolean onlineFlag;

    // 备注
    private String remark;

    // 有效标记
    private Boolean enabledFlag;

    // 操作码
    private String operationCode;

    // 错误信息
    private String errorMsg;

    // 数据状态
    private String dataStatus;

    // 数组分组ID
    private Long groupId;

    private Long id;


}
