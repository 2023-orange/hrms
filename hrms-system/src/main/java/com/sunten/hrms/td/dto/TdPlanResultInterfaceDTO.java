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
 * @since 2021-06-17
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdPlanResultInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 工牌号
    private String workCard;

    // 培训计划id
    private Long planId;

    // 员工出勤情况
    private String attendance;

    // 培训时长
    private Float duration;

    // 成绩
    private Float grade;

    // 满意度
    private String evaluate;

    // 是否签订培训协议书
    private Boolean needFlag;

    // 生效标记
    private Boolean enabledFlag;

    // 操作码
    private String operationCode;

    // 错误信息
    private String errorMsg;

    // 数据状态
    private String dataStatus;

    // 数组分组ID
    private Long groupId;

    // 姓名
    private String name;

    private Long id;

    private Double scoreLine;

    private Boolean matchImplementFlag;

    private String remarks;

    private String checkMethod;
}
