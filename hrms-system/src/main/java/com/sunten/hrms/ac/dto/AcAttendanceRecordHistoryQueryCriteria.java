package com.sunten.hrms.ac.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @author liangjw
 * @since 2020-10-15
 */
@Data
public class AcAttendanceRecordHistoryQueryCriteria implements Serializable {
    // 外层查询条件
    private String employeeName;

    private Long employeeId;

    private LocalDate beginDate;

    private String beginDateStr;

    private String endDateStr;

    private String endAndBeforeStr;

    private LocalDate endDate;

    private Long deptId;

    private Set<Long> deptIds;

    private Date testDateStart;

    private Date testDateEnd;

    private String disposeFlag;

    private Long hisID;

    // 处理意见
    private String targetResult;
    // 处理备注
    private String targetDescribes;

    /**
     *  @author：liangjw
     *  @Date: 2020/10/29 13:37
     *  @Description: null 全部， true 查异常， false查非异常
     */
    private Boolean abNormalFlag;

    private Set<Long> hisIds;

    // 打回的异常集合
    private List<Long> aacrIds;

    // 最新在用的打回异常集合
    private List<Long> aarhIds;

    private String hisIdsStr;

    // 模糊工号姓名
    private String queryName;

    // 是否有申请单号(true查有申请单号，其它不查)
    private Boolean requestFlag;

    // 申请单号状态(state = 'pass,wait,notpass', 其余不查)
    private String state;

    // 异常类型：1、上班期间打卡，2、上下班未打卡，3、单次数打卡，4、全天未打卡， 5、无排班
    private String exceptionType;

    // 用于区分普通人员、管理人员, 普通人员: normal  , 管理人员: charge
    private String peopleType;

    // 班长层控制
    private Boolean teamUnion;

    // 资料员层控制
    private Boolean docUnion;

    // 领导层控制
    private Boolean chargeUnion;

    // 个人层控制
    private Boolean personUnion;

    // 班长层参数
    private Boolean teamCommitFlag;

    private Set<Long> teamDeptIds;

    // 原始的数据权限范围
    private Set<Long> teamDsDeptIds;

    // 移除管理岗
    private Boolean teamRemoveFlag;

    private String teamCodeStatus;

    // 移除自身
    private Boolean teamRemoveSelfFlag;

    private Long teamRemoveSelfId;
    // 领导层使用班组层作代替时使用的参数
    // 开启领导线审批的查询条件
    private Boolean teamLowerLevel;

    // 登录的领导的员工id
    private Long higherLevelEmployeeId;

    private Boolean teamDeptIdsIsNull;

    // 授权范围语句开启
    private Boolean teamAuthLowerLevel;

    // 授权范围的参考人
    private Long higherLevelAuthorizationToEmployeeId;

    // 授权的id范围
    private List<Long> teamAuthDeptIds;

    // 资料员层参数
    private Boolean docCommitFlag;

    private Boolean docQueryFlag; // 资料员层最主要的查询控制，只要未提交、且被打回过的都在填写页上显示出来

    private Set<Long> docDeptIds;

    private Boolean docRemoveFlag;

    private String docCodeStatus;

    private Boolean docBatchCommitCodeStatusFlag; // 批量提交时的状态集合是否开启

    private String docMultiCodeStatus;

    // 个人层参数
    private Boolean personQueryFlag;

    private Long personEmployeeId;

    private String chara;

    // 管理层参数
    private Boolean chargeCommitFlag;

    private Set<Long> chargeDeptIds;

    private Boolean chargeRemoveFlag;

    // write为填写页， approval为审批页， watch 为查看页， 此标记只给部门树使用
    private String pageType;

    private Long  baseQueryEmpId;

}
