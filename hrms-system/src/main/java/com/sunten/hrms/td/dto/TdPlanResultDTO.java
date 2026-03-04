package com.sunten.hrms.td.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableField;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.util.Date;

/**
 * @author liangjw
 * @since 2021-06-16
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdPlanResultDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 员工id
    private Long employeeId;

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

    private Long id;

    @TableField(exist = false)
    private String name; // 姓名

    @TableField(exist = false)
    private String workCard; // 工牌号

    @TableField(exist = false)
    private  String deptName; // 部门

    @TableField(exist = false)
    private String department; // 科室

    @TableField(exist = false)
    private String  team; // 班组

    private Boolean editFlag;

    private String idNumber;


    @TableField(exist = false)
    private Date contractEndDate;
    @TableField(exist = false)
    private Date contractStartDate;

    // 是否有培训协议的标记
    private Boolean haveAgreementFlag;

    private Double scoreLine;

    private String remarks;

    private Boolean matchImplementFlag;

    private Integer resultEmployeeQuantity;// 结果参训人数

    private String checkMethod;


}
