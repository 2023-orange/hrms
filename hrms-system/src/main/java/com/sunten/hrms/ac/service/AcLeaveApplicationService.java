package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcLeaveApplication;
import com.sunten.hrms.ac.domain.HrUpdateLeaveInfoForm;
import com.sunten.hrms.ac.domain.LeaveReqForm;
import com.sunten.hrms.ac.domain.PmLeaveApplication;
import com.sunten.hrms.ac.dto.AcLeaveApplicationDTO;
import com.sunten.hrms.ac.dto.AcLeaveApplicationQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.ac.dto.PmLeaveApplicationDTO;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zouyp
 * @since 2023-05-29
 */
public interface AcLeaveApplicationService extends IService<AcLeaveApplication> {

    AcLeaveApplicationDTO insert(AcLeaveApplication hrLeaveNew);

    void delete();

    void delete(AcLeaveApplication hrLeave);

    void update(AcLeaveApplication hrLeaveNew);

    AcLeaveApplicationDTO getByKey();

    /**
     * 查询不分页
     * @param criteria
     * @return
     */
    List<AcLeaveApplicationDTO> listAll(AcLeaveApplicationQueryCriteria criteria);
    List<AcLeaveApplicationDTO> listAll2(AcLeaveApplicationQueryCriteria criteria);

    /**
     * 查询分页
     * @param criteria
     * @param pageable
     * @return
     */
    Map<String, Object> listAll(AcLeaveApplicationQueryCriteria criteria, Pageable pageable);

    void download(List<AcLeaveApplicationDTO> hrLeaveDTOS, HttpServletResponse response) throws IOException;

    /**
     * 获取个人年假信息
     * @param workCard 工牌号
     * @return 年假信息
     */
    Double getAnnualLeave(String workCard);
    /**
     * 获取个人事假信息
     * @param workCard 工牌号
     * @return 事假信息
     */
    Double getLeaveOfAbsence(String workCard);

    /**
     * 计算个人月度可调休时数
     * @param workCard
     * @return
     */
    Double getCompensatoryLeaveHoursWithMonth(String workCard);

    /**
     * 以工牌号为依据查询员工信息
     * @param workCard
     * @return
     */
    Map<String, String> getUserViews(String workCard);

    /**
     * 查询班组长list
     * @param params
     * @return
     */
    List<PmLeaveApplication> getUserForemanList(HashMap<String, String> params);

    /**
     * 获取用户部门主管列表
     * @param params
     * @return
     */
    List<PmLeaveApplication> getDepartmentHeads(HashMap<String, String> params);

    /**
     * 获取用户部门经理列表
     * @param params
     * @return
     */
    List<PmLeaveApplication> getMangerList(HashMap<String, String> params);
    /**
     * 从OA库查询上级领导列表
     * @param keyWord
     * @return
     */
    @MapKey("ValueField")
    Map<String, Object> getDirectorMap(String keyWord);

    /**
     * 从HR数据库中查询-请假的开始日期或结束日期是否跟审批中的日期是否有重复
     * @param startTime
     * @param endTime
     * @param workCard
     * @return
     */
    HashMap<String, Object> checkLeaveSubDataContinuous(String startTime, String endTime, String workCard, Float subTotalNumber);

    /**
     * 校验请假表单数据是否合规
     * @param leaveReqForm
     * @param oaOrderWordCard
     * @return
     */
    HashMap<String, String> checkSubmitData(LeaveReqForm leaveReqForm, String oaOrderWordCard);

    /**
     * 计算工作日天数
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getWorkDays(String startTime, String endTime);

    /**
     * 从OA端进入，根据OA号获取请假主表和子表
     *
     */
    HashMap<String,Object> getLeaveInfoByReqCode(String reqCode);


    /**
     * 从OA端进入，OA审批时修改请假主表
     *
     */
    void writeOaApprovalResult(AcLeaveApplication acLeaveApplication);



    /**
     * 根据申请编号查询所有请假信息
     * @param oaOrder
     * @return
     */
    HashMap<String, List<HashMap<String, Object>>> getLeaveInfo(String oaOrder);
    HashMap<String, List<HashMap<String, Object>>> getHrEditLeaveInfo(String oaOrder);
    /**
     * 育儿假逻辑处理
     * @param childrenDate
     * @param workCard
     * @return
     */
    HashMap<String, Object> logicalProcessing(String childrenDate, String workCard);

    /**
     * 回写申请单当前审批人节点信息
     * @param oaOrder
     * @param approvalNode
     * @param approvalEmployee
     * @return
     */
    HashMap<String, String> updateOaReq(String oaOrder, String approvalNode, String approvalEmployee);

    /**
     * 这个是局部刷新table时候再次获取申请单列表
     * @param workCard
     * @return
     */
    List<PmLeaveApplicationDTO> getLeaveFormList(String workCard);

    /**
     * 根据父母一方出生日期判断是否具备护理假
     * @param parentDate
     * @param workCard
     * @return
     */
    HashMap<String, Object> getNurseDays(String parentDate, String workCard);

    /**
     * 获取全公司部门List
     * @return
     */
    List<HashMap<String, String>> getDepartmentList();

    /**
     * 根据部门ID获取对应部门全部人员
     * @param departmentID
     * @return
     */
    List<HashMap<String, String>> getUserByDepartmentID(String departmentID);

    /**
     * 根据OA申请单号
     * @param oaOrder
     * @return
     */
    Integer cancelLeave(String oaOrder);

    /**
     * 反写撤销请假申请单的结果
     * @param oaOrder
     * @param approvalResult
     */
    void updateResultToHrLeave(String oaOrder, String approvalResult);


    /**
     * 獲取員工月度可調休時數
     * @param workCard
     * @return
     */
    Double getMonthlyRestDayOvertimeHours(String workCard);

    /**
     * 请假10太难以上自动匹配上级领导
     * @param department
     * @param administrativeOffice
     * @return
     */
    HashMap<String, String> autoMatchLeaderUser(String department, String administrativeOffice);

    /**
     * 人资修改假条信息
     * @param hrUpdateLeaveInfo
     * return
     */
    Integer updateHrChangeLeaveInfo(HrUpdateLeaveInfoForm hrUpdateLeaveInfo);
    /**
     * 获取人资修改的请假信息
     * @param oaOrder
     * @return
     */
    HashMap<String, Object> getHrChangeLeaveInfo(String oaOrder);


    Object updateHrLeaveEditInfo(String oaOrder, Integer version);
    /**
     * 根据部门名称去查到部门iD，然后去找到对应的高级领导
     * @param deptName
     * @return
     */

    HashMap<String, Object> getAcLeaveLeaderUser(String deptName);

    /**
     * 获取人资经理信息
     * @return
     */
    HashMap<String, Object> getHrManger();

    /**
     * 获取人事专员
     * @return
     */
    List<HashMap<String, Object>> getHrInfo();

    /**
     * 通知人资经理
     * @param oaOrder
     */
    void notifyHrManager(String oaOrder);

    /**
     * 中途上传附件需要通知人事专员
     * @param oaOrder
     */
    void notifyHr(String oaOrder);

    /**
     * 获取审批管理人员列表
     * @return
     */
    List<HashMap<String, Object>> getAcAuthorizationList(Integer deptId);

    /**
     * 获取审批管理行详情
     * @param id
     * @param deptId
     * @return
     */
    List<HashMap<String, Object>> getApprovalDetail(Integer id, Integer deptId);

    /**
     * 更新审批管理行
     * @param id
     * @param deptId
     * @param team_work_card
     * @param department_work_card
     * @param manger_work_card
     * @param leader_work_card
     * @param teamAuthorizeWorkCard
     * @param departmentAuthorizeWorkCard
     * @param mangerAuthorizeWorkCard
     * @param leaderAuthorizeWorkCard
     */
    void updateApproval(Integer id, Integer deptId,String team_work_card,String department_work_card,  String manger_work_card, String leader_work_card,String teamAuthorizeWorkCard, String departmentAuthorizeWorkCard, String mangerAuthorizeWorkCard, String leaderAuthorizeWorkCard);

    /**
     * 新增审批
     * @param dept_id
     * @param teamWorkCard
     * @param teamAuthorizeWorkCard
     * @param departmentWorkCard
     * @param departmentAuthorizeWorkCard
     * @param mangerWorkCard
     * @param mangerAuthorizeWorkCard
     * @param leaderWorkCard
     * @param leaderAuthorizeWorkCard
     * @return
     */
    Integer addApprovalDetail(Integer dept_id, String teamWorkCard, String teamAuthorizeWorkCard, String departmentWorkCard, String departmentAuthorizeWorkCard, String mangerWorkCard, String mangerAuthorizeWorkCard, String leaderWorkCard, String leaderAuthorizeWorkCard);

    /**
     * 从HR获取当前请假申请人的对应班长列表
     * @param workCard
     * @return
     */
    List<Map<String, Object>> getTeamLeaderList(String workCard);

    /**
     * 从HR获取当前请假申请人的对应主管列表
     * @param workCard
     * @return
     */
    List<Map<String, Object>> getDepartmentLists(String workCard);

    /**
     * 从HR获取当前请假申请人的对应经理列表
     * @param workCard
     * @return
     */
    List<Map<String, Object>> getHrMangerList(String workCard);

    /**
     * 从HR获取当前请假申请人的对应上级领导
     * @param workCard
     * @return
     */
    List<Map<String, Object>> getHrLeaderList(String workCard);

    /**
     * 自动匹配上级领导
     * @param workCard
     * @return
     */
    HashMap<String, String> autoMatchNewLeaderUser(String workCard);

    /**
     * 更新审批申请管理页面的enabledFlag
     * @param id
     * @param deptId
     * @param enabledFlag
     */
    void changeAcApprovalDepartmentStatus(Integer id, Integer deptId, Integer enabledFlag);

    Double getTeamHourAnnualYear(String workCard);

    Double getTeamUsedHourAnnualYear(String workCard);

    void leaveApplicationNotifyHr(String oaOrder, String mailUser);
    // 根据开始日期查询当前日期全部上班时间段
    HashMap<String, Object> getStartTimeList(String workCard, String startTime);

    HashMap<String, Object> getEndTimeList(String workCard, String endTime);

    HashMap<String, Object> checkLeaveRule(String startTime, String endTime, String workCard);

    Integer shiftChecks(String workCard);

    Integer getWorkDaysSecond(String startTime, String endTime);

    PmEmployeeDTO getByEmployeeId(Long id);


    /**
     * 从HR数据库中查询-请假的开始日期或结束日期是否跟审批中的日期是否有重复
     * @param startTime
     * @param endTime
     * @param workCard
     * @return
     */
    HashMap<String, Object> newCheckLeaveSubDataContinuous(String startTime, String endTime, String workCard, Float subTotalNumber);

    Map<String, Object> listAllLeaveStatistics(AcLeaveApplicationQueryCriteria criteria, Pageable pageable);
}
