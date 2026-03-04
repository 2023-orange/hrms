package com.sunten.hrms.re.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.re.domain.ReDemandJobDescribes;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.time.LocalDateTime;
    import java.util.List;

/**
 * @author liangjw
 * @since 2021-04-23
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ReDemandJobDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 用人需求id
    private Long demandId;
    //编号
    private String demandCode;
    //剩余人数
    private String remainingPersonnel;
    // 岗位名称
    private String jobName;

    private Long jobId;

    // 岗位人数
    private Integer quantity;

    // 岗位素质要求
    private String baseCondition;

    // 是否特殊岗位
    private Boolean specialFlag;

    // 特殊入职体检项目
    private String specialMidecal;

    // 是否需要岗位说明书
    private Boolean needFlag;

    // 生效标记
    private Boolean enabledFlag;

    private Long id;

    private Boolean realType; // 用于前台控制删除是否请求数据库

    private String realFlag;

    private Boolean showBox;

    private List<ReDemandJobDescribesDTO> demandJobDescribesList;

    private String jobNameById;

    // 危险因素
    private String riskFactors;

    // 班次信息
    private String teamMes;

    // 岗位同期当年人数
    private Integer jobFirstCount;
    // 岗位同期去年人数
    private Integer jobSecondCount;
    // 岗位同期前年人数
    private Integer jobThirdCount;
    // 在占用的数量
    private Integer inUsedQuantity;
    // 通过的数量
    private Integer passQuantity;
    // 学历
    private String education;

    // 性别
    private String sex;

    // 年龄范围
    private String age;

    // 工作年限
    private String workAge;

    // 工作经验年限
    private String workExperenceAge;

    // 必备证件
    private String necessaryCertificates;

    // 其它技能及要求
    private String otherSkillsRequirements;

    // 专业要求
    private String skillsRequirements;

    // 岗位类别
    private String jobClass;

    // 记录手动开放的用户id
    private Long demandOpenBy;

    // 记录手动开放的操作时间
    private LocalDateTime demandOpenTime;

    // 用于区分是否在审批前提交
    private Boolean addBeforeApprovalFlag;

    // 是否需要检测
    private Boolean needCheckFlag;

    // 岗位当前人数
    private Integer jobCurrentCount;

    // 同意人数
    private Integer agreeNum;


}
