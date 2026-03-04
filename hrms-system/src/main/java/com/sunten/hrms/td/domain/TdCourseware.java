package com.sunten.hrms.td.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.tool.domain.ToolLocalStorage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 课件资料表
 * </p>
 *
 * @author xukai
 * @since 2021-06-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdCourseware extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 课件名称
     */
    @NotBlank
    private String name;

    /**
     * 归属课程或项目类型
     */
    private String dependentProject;

    /**
     * 归属课程或项目id
     */
    private Long dependentId;

    /**
     * 课件开发人员类别（内部、外部）
     */
    @NotBlank
    private String employeeType;

    /**
     * 内部人员工号
     */
    private String inWorkCard;

    /**
     * 内部人员姓名
     */
    private String inName;

    /**
     * 内部员工id
     */
    private Long inEmployeeId;

    /**
     * 外部人员姓名
     */
    private String outName;

    /**
     * 外部人员机构
     */
    private String outOrganization;

    /**
     * 课件适用对象
     */
    private String useObject;

    /**
     * 课件权限设置
     */
    @NotBlank
    private String jurisdictionSetting;

    /**
     * 附件id
     */
//    @NotNull
//    private Long fileId;
    private ToolLocalStorage storage;
    /**
     * 可下载标识
     */
    @NotNull
    private Boolean downloadFlag;

    /**
     * 有效标记
     */
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private List<TdCoursewareEmployee> employees; // 仅限选中员工子表


    private String approvalStatus; // 审批状态，wait审批中，pass审批通过，notpass驳回

    private String oaOrder; // OA审批单号

    private String lastVerdict; // 最后审批意见

    private LocalDateTime approvalEndTime; // 审批通过时间

    private Boolean selfFlag;
}
