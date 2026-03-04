package com.sunten.hrms.td.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 培训计划表
 * </p>
 *
 * @author liangjw
 * @since 2021-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdPlan extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 培训名称
     */
    @NotBlank
    private String trainingName;

    /**
     * 培训方式
     */
    private String trainingMethod;

    /**
     * 培训内容
     */
    private String trainingContent;

    /**
     * 培训目的
     */
    private String trainingPurpose;

    /**
     * 培训级别
     */
    private String trainingLevel;

    /**
     * 专业分类
     */
    private String professionClassify;

    /**
     * 所属部门
     */
    private String dependenceDept;

    /**
     * 主办部门
     */
    private String hostDept;

    /**
     * 讲师
     */
    private String teacher;

    /**
     * 参加人员
     */
    private String employeeDescribes;

    /**
     * 时间
     */
    private String planDate;

    /**
     * 参训人数
     */
    private Integer employeeQuantity;

    /**
     * 预算(元)
     */
    @NotNull
    private BigDecimal planMoney;

    /**
     * 是否线上审批
     */
    @NotNull
    private Boolean onlineFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;

    /**
     * 计划进度(实施、作废、取消)
     */
    @NotBlank
    private String planStatus;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private Long planChargeId;

    private Long deptChargeId;

    private String planChargeName; // 计划负责人

    private String deptChargeName; // 部门负责人

    private String planType; // 计划类别（年度内计划，年度外计划）

    private String planChargeWorkCard; // 计划负责人工号

    private String deptChargeWorkCard; // 部门负责人工号

    private String deptChargeEmail; // 部门负责人邮箱

    private String planChargeEmail; // 计划负责人邮箱

    private TdPlanImplement planImplement; // 实施数据

    private List<TdPlanEmployee> inEmpList; // 参训人员列表

    private List<TdPlanEmployee> inTeacherList; // 讲师列表

    private List<TdPlanImplementDept> inDeptList; // 所属部门列表

    private Boolean showFlag;

    private List<List<TdPlanEmployee>> employeesShow;

    private List<List<TdPlanEmployee>> teacherShow;

    private List<List<TdPlanImplementDept>> deptShow;

    private String changeDescribes;

    private String attribute1;

    private String changeOaOrder;

    private String changeCurrentNode;

    private String changeCurrentPerson;

    private String changePlanDate;

    private Boolean addHistoryFlag; // 历史插入控制标记

    private Long hostDeptId; // 主办部门id

    private String commitType; // 接收save或者commit

    private Boolean realEditFlag; // 是否为编辑

    private Boolean passFlag;

    private Boolean implementOrderFlag; // 判定是否曾经提交过实施

    private Integer totalEmployeeQuantity; // 实施人数

    private Integer resultEmployeeQuantity;// 参训人数

    private String trainingNo; // 培训编号

    private List<TdPlanCheckMethod> checkMethodList; // 考核方式集合




}
