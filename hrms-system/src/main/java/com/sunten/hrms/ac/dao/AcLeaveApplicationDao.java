package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.*;
import com.sunten.hrms.ac.dto.AcLeaveApplicationQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.ac.dto.AcVacateQueryCriteria;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zouyp
 * @since 2023-05-29
 */
@Mapper
@Repository
public interface AcLeaveApplicationDao extends BaseMapper<AcLeaveApplication> {

    int insertAllColumn(AcLeaveApplication hrLeave);

    int deleteByKey();

    int deleteByEntityKey(AcLeaveApplication hrLeave);

    int updateAllColumnByKey(AcLeaveApplication hrLeave);

    AcLeaveApplication getByKey(@Param(value = "id") int id);

    List<AcLeaveApplication> listAllByCriteria(@Param(value = "criteria") AcLeaveApplicationQueryCriteria criteria);

    List<AcLeaveApplication> listAllByCriteriaPage(@Param(value = "page") Page<AcLeaveApplication> page, @Param(value = "criteria") AcLeaveApplicationQueryCriteria criteria);

    /**
     * 查询个人当年年假总天数
     * @param workCard
     * @return
     */
    Double getAnnualLeave(@Param(value = "workCard") String workCard);

    /**
     * 根据工牌号查询人员信息
     * @param workCard
     * @return
     */
    @MapKey(value = "workCard")
    Map<String, String> getUserViews(@Param("workCard") String workCard);


    /**
     * 根据 部门-科室-班组-获取到车间人员的班组长列表
     * @param params
     * @return
     */
    List<PmLeaveApplication> getUserForemanList(HashMap<String, String> params);

    /**
     * 根据 部门-科室 获取当前登录人员的部门主管列表
     * @param params
     * @return
     */
    List<PmLeaveApplication> getDepartmentHeads(HashMap<String, String> params);

    /**
     * 根据 部门 获取经理列表
     * @param params
     * @return
     */
    List<PmLeaveApplication> getMangerList(HashMap<String, String> params);

    /**
     * 从HR数据库获取已经使用的年假
     * @param workCard
     * @return
     */
    Double getUsedAnnualLeave(@Param("workCard") String workCard);

    /**
     * 从HR数据库获取已经使用的事假
     * @param workCard
     * @return
     */
    Double getUsedAbsenceLeaveDays(@Param("workCard") String workCard);

    /**
     * 检查请假的开始日期或结束日期是否跟审批中的日期是否有重复
     * @param start_time
     * @param end_time
     * @param workCard
     * @return
     */
    Integer checkLeaveSubDataContinuous(@Param("start_time") String start_time, @Param("end_time") String end_time, @Param("workCard") String workCard);

    /**
     * 获取用户除了撤销之外的所有请假单
     * @param workCard
     * @return
     */
    List<HashMap<String, String>> GetUserNotCancel(@Param("workCard") String workCard);

    /**
     * 工作日连续性检查
     * @param newST
     * @param newET
     * @param oldST
     * @param oldET
     * @return
     */
    Integer checkContinuousDate(@Param("newST") String newST, @Param("newET") String newET, @Param("oldST") String oldST, @Param("oldET") String oldET);

    /**
     * 添加主表记录
     * @param param1
     * @return
     */
    int addMainTableInfo(HashMap<String, Object> param1);

    /**
     * 插入子表数据
     * @param subDataList
     * @return
     */
    Integer addSubLeaveInfo(@Param("subDataList")List<LeaveSubData> subDataList);

    /**
     * 计算工作日天数
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getWorkDays(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 获取请假主表
     *
     */
    List<HashMap<String, Object>> getLeaveInfoByReqCode(@Param("reqCode") String reqCode);

    /**
     * 获取请假子表
     *
     */
    List<HashMap<String, Object>> getLeaveSubInfoByReqCode(@Param("reqCode") String oaOrder);



    /**
     * 审批结束时
     *
     */
    int updateByApprovalEnd(AcLeaveApplication acLeaveApplication);

    /**
     * 中间节点审批时
     *
     */
    int updateByApprovalUnderwar(AcLeaveApplication acLeaveApplication);

    /**
     * 查询主表请假信息
     * @param oaOrder
     * @return
     */
    List<HashMap<String, Object>> getPmLeaveInfo(@Param("oa_order") String oaOrder);

    /**
     * 查询子表请假信息
     * @param oaOrder
     * @return
     */
    List<HashMap<String, Object>> getPmLeaveSubInfo(@Param("oa_order") String oaOrder);

    /**
     * 查询对应周岁已使用的育儿假
     * @param workCard
     * @param childDate
     * @param endDate
     * @return
     */
    int getUsedParentalLeave(@Param("workCard") String workCard, @Param("childDate") String childDate, @Param("endDate") String endDate);

    /**
     * 查询对应周岁已使用的育儿假的次数
     * @param workCard
     * @param childDate
     * @param endDate
     * @return
     */
    int getParentalLeaveCount(@Param("workCard") String workCard, @Param("childDate") String childDate, @Param("endDate") String endDate);

    /**
     *  反写当前申请当审批节点信息
     * @param oa_order
     * @param approval_node
     * @param approval_employee
     * @return
     */
    int updateOaReq(@Param("oa_order") String oa_order, @Param("approval_node")  String approval_node, @Param("approval_employee")  String approval_employee);

    /**
     * 局部刷新重新获取请假list
     * @param workCard
     * @return
     */
    List<PmLeaveApplication> getLeaveFormList(@Param("workCard") String workCard);

    /**
     * 计算本年度请了多少天护理假
     * @param workCard
     * @return
     */
    int getUsedNurseDays(@Param("workCard") String workCard);

    /**
     * 计算本年度请了几次护理假
     * @return
     */
    int getUsedNurseDaysCount(@Param("workCard") String workCard);

    /**
     * 获取全公司部门list
     * @return
     */
    List<HashMap<String, String>> getDepartmentList();

    /**
     * 根据部门Id获取对应人员
     * @param departmentID
     * @return
     */
    List<HashMap<String, String>> getUserByDepartmentID(@Param("departmentID") String departmentID);

    /**
     * 根据OA申请单号来撤销主表记录
     * @param oaOrder
     * @return
     */
    Integer cancelLeaveMain(@Param("oaOrder") String oaOrder);

    /**
     * 根据OA申请单号来撤销子表记录
     * @param oaOrder
     */
    Integer cancelLeaveSub(@Param("oaOrder") String oaOrder);

    /**
     * 反写撤销请假申请单结果
     * @param oaOrder
     * @param approvalResult
     */
    void updateResultToHrLeave(@Param("oa_order") String oaOrder, @Param("approvalResult") String approvalResult);

    /**
     * 獲取員工月度加班工時
     * @param workCard
     * @return
     */
    Double getMonthOverTime(@Param("work_card") String workCard);

    /**
     * 获取员工月度使用调的加班工时
     * @param workCard
     * @return
     */
    Double getUsedMonthOverTime(String workCard);

    /**
     * 请假10天自动匹配上级领导
     * @param department
     * @param administrativeOffice
     * @return
     */
    HashMap<String, String> autoMatchLeaderUser(@Param("department") String department, @Param("administrativeOffice") String administrativeOffice);

    /**
     * 更新请假主表信息
     * @param oaOrder
     * @param hrTotal
     * @param modifyReason
     * @param version
     */
    void updateMainInfo(@Param("oaOrder") String oaOrder, @Param("hrTotal") Float hrTotal, @Param("modifyReason") String modifyReason, @Param("version") Integer version);

    /**
     *  新增子表记录
     * @param subDataList
     * @return
     */
    Integer insertHrSubInfo(List<HrUpdateLeaveInfoSub> subDataList);

    /**
     * 获取HR修改后的主表记录
     * @param oaOrder
     * @return
     */
    HashMap getMainInfo(@Param("oaOrder") String oaOrder);

    /**
     * 获取HR新增版本的子表请假记录，就获取最新一条
     * @param oaOrder
     * @return
     */
    HashMap getHrChangeNewLeaveSubInfo(@Param("oaOrder") String oaOrder);

    /**
     * 逻辑删除人资修改的子表信息
     * @param oaOrder
     * @param version
     * @return
     */
    Integer updateHrLeaveEditInfo(@Param("oaOrder") String oaOrder, @Param("version") Integer version);

    /**
     * 根据部门名称获取部门ID
     * @param deptName
     * @return
     */
    Integer getDeptId(@Param("dept_name") String deptName);

    /**
     * 获取部门parentId
     * @param parentID
     */
    HashMap getParentId(@Param("parentID") Integer parentID);

    /**
     * 获取人资经理
     * @return
     */
    HashMap<String, Object> getHrManger();

    /**
     * 获取人事专员
     * @return
     */
    List<HashMap<String, Object>> getHrInfo();

    /**
     * 获取人资经理的邮箱
     * @return
     */
    String getHrManagerEmail();

    /**
     * 请假申请人再次上传附件的时候需要修改
     */
    void updateToBeUploadedFlag(@Param("oaOrder") String oaOrder);


    /**
     * 获取人事专员邮箱
     * @return
     */
    List<String> getHrEmails();

    /**
     * 获取审批人员列表
     * @return
     */
    List<HashMap<String, Object>> getAuthorizationList(@Param("deptId") Integer deptId);

    /**
     * 获取班组长map
     * @param deptId
     * @return
     */
    HashMap<String, Object> getTeamList(@Param("deptId") Integer deptId);

    /**
     * 获取主管map
     * @param deptId
     * @return
     */
    HashMap<String, Object> getDepartmentMap(@Param("deptId") Integer deptId);

    /**
     * 经理
     * @param deptId
     * @return
     */
    HashMap<String, Object> getMangerMap(@Param("deptId") Integer deptId);

    /**
     * 领导
     * @param deptId
     * @return
     */
    HashMap<String, Object> getLeaderMap(@Param("deptId") Integer deptId);

    /**
     * 获取审批管理行详情
     * @param id
     * @param deptId
     * @return
     */
    List<HashMap<String, Object>> getApprovalDetail(@Param("id") Integer id, @Param("deptId") Integer deptId);

    /**
     * 更新审批行
     * @param id
     * @param deptId
     * @param teamAuthorizeWorkCard
     * @param departmentAuthorizeWorkCard
     * @param mangerAuthorizeWorkCard
     * @param leaderAuthorizeWorkCard
     */
    void updateApproval(@Param("id") Integer id, @Param("deptId") Integer deptId,
                        @Param("team_leader_job_id") Integer team_leader_job_id,
                        @Param("team_leader_id") Integer team_leader_id,
                        @Param("department_head_job_id") Integer department_head_job_id,
                        @Param("department_head_id") Integer department_head_id,
                        @Param("manger_job_id") Integer manger_job_id,
                        @Param("manger_id") Integer manger_id,
                        @Param("leader_job_id") Integer leader_job_id,
                        @Param("leader_id") Integer leader_id,
                        @Param("teamAuthorizeWorkCard") String teamAuthorizeWorkCard,
                        @Param("departmentAuthorizeWorkCard") String departmentAuthorizeWorkCard,
                        @Param("mangerAuthorizeWorkCard") String mangerAuthorizeWorkCard,
                        @Param("leaderAuthorizeWorkCard") String leaderAuthorizeWorkCard);


    /**
     * 查询新部门deptId
     * @param deptName
     * @return
     */
    Integer getNewDeptId(@Param("deptName") String deptName, @Param("department") String department);

    /**
     * 获取job_id
     * @param workCard
     * @return
     */
    Integer getNewJobId(@Param("workCard") String workCard);

    /**
     * 增加部门管理审批行
     * @param deptId
     * @param teamLeaderJobId
     * @param departmentHeadJobId
     * @param mangerJobId
     * @param leaderJobId
     * @param teamAuthorizeWorkCard
     * @param departmentAuthorizeWorkCard
     * @param mangerAuthorizeWorkCard
     * @param leaderAuthorizeWorkCard
     * @param enabled
     * @return
     */
    Integer addApprovalDetail(@Param("deptId") Integer deptId, @Param("teamLeaderJobId") Integer teamLeaderJobId,
                              @Param("departmentHeadJobId") Integer departmentHeadJobId, @Param("mangerJobId") Integer mangerJobId,
                              @Param("leaderJobId") Integer leaderJobId, @Param("teamAuthorizeWorkCard") String teamAuthorizeWorkCard,
                              @Param("departmentAuthorizeWorkCard") String departmentAuthorizeWorkCard, @Param("mangerAuthorizeWorkCard") String mangerAuthorizeWorkCard, @Param("leaderAuthorizeWorkCard") String leaderAuthorizeWorkCard,@Param("enabled_flag") Integer enabled);

    /**
     * 获取班长列表
     * @param deptId
     * @return
     */
    List<Map<String, Object>> getTeamLeaderList(@Param("deptId") Integer deptId);

    /**
     * 根据工牌号获取dept——id
     * @param workCard
     * @return
     */
    Integer getNewDeptIdbyWorkCard(@Param("workCard") String workCard);

    /**
     * 获取主管列表
     * @param deptId
     * @return
     */
    List<Map<String, String>> getDepartmentLists(@Param("deptId") Integer deptId);

    /**
     * 获取经理列表
     * @param deptId
     * @return
     */
    List<Map<String, String>> getHrMangerList(@Param("deptId") Integer deptId);

    /**
     * 获取领导上级列表
     * @param deptId
     * @return
     */
    List<Map<String, String>> getHrLeaderList(Integer deptId);

    /**
     * 自动匹配上级领导-新
     * @param deptId
     * @return
     */
    HashMap<String, String> autoMatchNewLeaderUser(Integer deptId);

    /**
     * 更新请假审批管理页面的状态
     * @param id
     * @param deptId
     * @param enabledFlag
     */
    void changeAcApprovalDepartmentStatus(@Param("id") Integer id, @Param("deptId") Integer deptId, @Param("enabledFlag") Integer enabledFlag);

    Map<String, Object> getBrokerDepartmentList(@Param("deptId") Integer deptId);

    Map<String, Object> getDeptMainMap(@Param("deptId") Integer deptId);

    List<HashMap<String, Object>>  getAllHrChangeNewLeaveSubInfo(@Param("oaOrder") String oaOrder);

    List<HashMap<String, Object>> getHrEditLeaveSubInfo(@Param("oaOrder") String oaOrder);

    HashMap<String, String> checkMail(@Param("mailUser") String mailUser);

    String getUserNameByOaOrder(@Param("oaOrder") String oaOrder);

    Object getUserNameByWorkCard(@Param("workCard")  String teamAuthorizeWorkCard);

    HashMap<String, Object> getStartTimeList(@Param("workCard") String workCard, @Param("startTime") String startTime);

    HashMap<String, Object> getEndTimeList(@Param("workCard")String workCard, @Param("endTime") String endTime);

    HashMap checkLeaveRule(@Param("formattedDate") String formattedDate, @Param("workCard") String workCard);

    Integer getRestDay(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("workCard") String workCard);

    Integer shiftChecks(@Param("workCard") String workCard);

    Integer getWorkDaysSecond(String startTime, String endTime);

    List<AcLeaveForm> getLeaveFormByDate(@Param(value = "dateFrom") LocalDate dateFrom, @Param(value = "dateTo") LocalDate dateTo);

    List<HashMap<String, Object>> getAuthorizationListAll();

    Integer getNewDeptIdByDept(@Param("department")String department);

    Integer getDeptIdBySectionName(@Param("deptName") String deptName);

    Integer getDeptIdByDepartment(@Param("department") String department);

    String getWorkCardById(@Param("teamLeaderEmployeeId") Integer teamLeaderEmployeeId);

    Integer getEmployeeIdByWordCard(@Param("work_card")String teamWorkCard);

    List<HashMap<String, Object>> GetLeftLeaveNotCancel(@Param("startTime") String startTime, @Param("workCard") String workCard);

    List<HashMap<String, Object>> GetRigthLeaveNotCancel(@Param("endTime") String endTime, @Param("workCard") String workCard);

    String getFirstMaxWorkDate(@Param("startTime") String startTime);

    String getFirsminWorkDate(@Param("rEndTime") String rEndTime);

    Integer getDeptIdByWorkCard(@Param("workCard") String workCard);

    List<AcVacate> getList(@Param("oaOrder") String oaOrder);

    AcVacate getAcVacateByRequisitionCode(@Param("OaOrder") String OaOrder);

    List<AcVacate> getList2();

    List<AcLeaveApplication> listAllLeaveStatistics(@Param(value = "page") Page<AcLeaveApplication> page, @Param(value = "criteria") AcLeaveApplicationQueryCriteria criteria);

    List<AcLeaveApplication> listAllByCriteria2(@Param(value = "criteria") AcLeaveApplicationQueryCriteria criteria);

    List<HrUpdateLeaveInfoSub> getSubLeaveInfoList(@Param("oaOrder") String oaOrder, @Param("version") Integer version);

    List<HashMap<String, Object>> getSubLeaveInfoList2(@Param("oaOrder") String oaOrder, @Param("version") Integer version, @Param("tempSubIds") List<Long> tempSubIds);

    String getJobNameByEmployeeId(@Param("employeeId") Long employeeId);

    Long getDeptIdByEmployeeId(@Param("employeeId") Long employeeId);

    HashMap<String, Object> getCurrentMap(@Param("deptId") Long deptId);
}
