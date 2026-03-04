package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
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
 * 体检申请子表
 * </p>
 *
 * @author xukai
 * @since 2021-04-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmMedicalLine extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 体检申请主表ID
     */
    private Long medicalId;
    private PmMedical medical;
    /**
     * 人员信息
     */
    private PmEmployee pmEmployee;
    /**
     * 作业类别及危害因素
     */
    private String workAndHazard;

    /**
     * 岗位名称
     */
    private String jobName;

    /**
     * 岗位id
     */
    private Long jobId;

    /**
     * 人员名称
     */
    private String employeeName;

    private String workCard;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 体检类别
     */
    private String medicalClass;

    /**
     * 相关id(离职、调动申请等的id)
     */
    private Long withId;

    /**
     * 体检项目名称
     */
    private String medicalName;
    /**
     * 具体体检项目
     */
    private List<PmMedicalLineRelevance> medicalLineRelevanceList;

    /**
     * 体检结果标记
     */
    private String medicalResult;

    /**
     * 体检备注
     */
    private String remake;

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;
    /**
     * 人员ID
     */
    private Long employeeId;
    // 修改标记，前台传到后台
    private Boolean editFlag;

    private LocalDateTime firstTime;
    /**
     * 部门
     */
    private  String deptName;
    /**
     * 科室
     */
    private String department;

    private String harmFactory;

    /**
     * 调到部门
     */
    private  String toDeptName;
    /**
     * 调到科室
     */
    private String toDepartment;
    /**
     * 调到班组
     */
    private String toTeamName;
    /**
     * 调到岗位
     */
    private String toJobName;
    /**
     * 调到岗位ID
     */
    private String toJobId;
    /**
     * 原部门
     */
    private  String fromDeptName;
    /**
     * 原科室
     */
    private String fromDepartment;
    /**
     * 原班组
     */
    private String fromTeamName;
}
