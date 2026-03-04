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
import java.util.List;

/**
 * <p>
 * 岗位体检表
 * </p>
 *
 * @author xukai
 * @since 2021-04-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmMedicalJob extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 部门名称
     */
    @NotBlank
    private String deptName;

    /**
     * 科室名称
     */
    private String officeName;

    /**
     * 班组名称
     */
    private String teamName;

    /**
     * 对应部门科室班组id
     */
    @NotNull
    private Long deptId;

    /**
     * 岗位名称
     */
    @NotBlank
    private String jobName;

    /**
     * 岗位id
     */
    @NotNull
    private Long jobId;

    /**
     * 岗位类别
     */
    @NotBlank
    private String jobClass;
    /**
     * 危害因素
     */
    private String harmFactory;

    /**
     * 职业禁忌症
     */
    private String jobTuboo;
    /**
     * 上岗体检项目字符串
     */
    private String goWorkStr;
    /**
     * 在岗体检项目字符串
     */
    private String inWorkStr;
    /**
     * 离岗体检项目字符串
     */
    private String outWorkStr;
    /**
     * 上岗体检项目子集
     */
    private List<PmMedicalRelevance> goMedicalRelevanceList;
    /**
     * 在岗体检项目子集
     */
    private List<PmMedicalRelevance> inMedicalRelevanceList;
    /**
     * 离岗体检项目子集
     */
    private List<PmMedicalRelevance> outMedicalRelevanceList;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


}
