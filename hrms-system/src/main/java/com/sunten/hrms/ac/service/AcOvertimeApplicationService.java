package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcOvertimeApplication;
import com.sunten.hrms.ac.domain.OvertimeApplicationForm;
import com.sunten.hrms.ac.domain.OvertimeLeaveCheckList;
import com.sunten.hrms.ac.domain.ReviewFormData;
import com.sunten.hrms.ac.dto.AcOvertimeApplicationDTO;
import com.sunten.hrms.ac.dto.AcOvertimeApplicationQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
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
 * @since 2023-10-16
 */
public interface AcOvertimeApplicationService extends IService<AcOvertimeApplication> {

    AcOvertimeApplicationDTO insert(AcOvertimeApplication overtimeApplicationNew);

    void delete(Integer id);

    void delete(AcOvertimeApplication overtimeApplication);

    void update(AcOvertimeApplication overtimeApplicationNew);

    AcOvertimeApplicationDTO getByKey(Integer id);

    List<AcOvertimeApplicationDTO> listAll(AcOvertimeApplicationQueryCriteria criteria);

    Map<String, Object> listAll(AcOvertimeApplicationQueryCriteria criteria, Pageable pageable);

    void download(List<AcOvertimeApplicationDTO> overtimeApplicationDTOS, HttpServletResponse response) throws IOException;

    /**
     * 获取加班人员信息列表
     * @param workCard
     * @param deptName
     * @return
     */
    List<HashMap<String, Object>> getMySelfOvertimeListInfo(String workCard, String deptName, String departmentName);

    /**
     * 从加班时数备份表获取当月加班时数
     * @param deptName
     * @return
     */
    HashMap<String, Object> getDeptUsedOvertimeInfo(String deptName, String department, String groups, String workCard);
    /**
     * 检查这次申请的加班是否重复
     * @param overtimeListInfoList
     * @return
     */

    /**
     * 检查这次申请的加班是否重复
     * @param startTime
     * @param endTime
     * @param workCard
     * @return
     */
    HashMap<String, Object> newCheckOvertimeInfo(String startTime, String endTime, String workCard);

    /**
     * 提交表单信息
     * @param overtimeApplicationForm
     * @param oaOrderWordCard
     * @return
     */
    HashMap<String, String> submitData(OvertimeApplicationForm overtimeApplicationForm, String oaOrderWordCard);

    /**
     * 反写当前加班审批信息
     * @param oaOrder
     * @param approvalNode
     * @param approvalEmployee
     * @return
     */
    HashMap<String, String> updateOaReq(String oaOrder, String approvalNode, String approvalEmployee);

    /**
     * 撤销加班申请
     * @param oaOrder
     * @return
     */
    Integer cancelOvertime(String oaOrder);

    /**
     * 反写撤销结果到加班申请主表
     * @param oaOrder
     * @param approvalResult
     */
    void updateResultToHrOvertime(String oaOrder, String approvalResult);

    /**
     * 根据OA申请单号获取加班信息
     * @param checkOaOrder
     * @return
     */
    HashMap<String, List<HashMap<String, Object>>> getOvertimeInfo(String checkOaOrder);

    /**
     * 获取所有部级部门信息
     * @return
     */
    List<HashMap<String, Object>> getAllDepartment();

    /**
     * 获取部门下的科室
     * @param parentId
     * @return
     */
    List<HashMap<String, Object>> getDepartmentByParentId(String parentId);

    /**
     * 添加加班复核人员信息
     * @param deptId
     * @param sectionId
     * @param workCard
     * @param createBy
     */
    void addOvertimeReviewUser(Integer deptId, Integer sectionId, String workCard, String createBy);

    /**
     * 获取所有有加班复核人员信息
     * @return
     */
    List<HashMap<String, Object>> getOvertimeReviewInfo();

    /**
     * 根据主键ID获取复核人员信息
     * @param id
     * @return
     */
    HashMap<String, Object> getOvertimeReviewUserInfo(Integer id);

    /**
     * 简单查询
     * @param name
     * @param value
     * @return
     */
    List<HashMap<String, Object>> simpleQuery(String name, String value);

    /**
     * 更新复核人员信息
     * @param id
     * @param deptId
     * @param sectionId
     * @param workCard
     */
    void updateOvertimeReview(Integer id, Integer deptId, Integer sectionId, String workCard);

    /**
     * 删除复核人员信息
     * @param id
     */
    void deleteOvertimeReviewInfo(Integer id);

    Map<String, Object> getOvertimeApplicationReviews(AcOvertimeApplicationQueryCriteria criteria, Pageable pageable, List<HashMap<String, Object>> checkList);

    /**
     * 检查当前用户是否是复核人员
     * @param workCard
     * @return
     */
    List<HashMap<String, Object>> checkReviewUser(String workCard);

    /**
     * 获取加班日期近三天的上班打卡记录
     * @param workCard
     * @param endTime
     * @return
     */
    List<HashMap<String, Object>> getRecentlyRecord(String workCard, String endTime);

    /**
     * 更新复核信息
     * @param updatedSubFormData
     */
    void updateReviewFormData(List<ReviewFormData> updatedSubFormData);

    /**
     * 加班申请
     * @param criteria
     * @param pageable
     * @param checkMonth
     * @param isHr
     * @param employeeType
     * @param department
     * @param administrativeOffice
     * @return
     */
    List<OvertimeLeaveCheckList> getOvertimeLeaveStatisticsQueryList(AcOvertimeApplicationQueryCriteria criteria, Pageable pageable, String checkMonth, Boolean isHr, Integer employeeType, String department, String administrativeOffice, String groups);

    HashMap<String, List<HashMap<String, Object>>> getOvertimeLeaveInfoByCheckMonthAndWorkCard(String checkMonth, String workCard);

    void downloadOvertimeLeaveList(List<OvertimeLeaveCheckList> overtimeLeaveStatisticsQueryList, HttpServletResponse response) throws IOException;

    List<OvertimeLeaveCheckList> getOvertimeLeaveStatisticsQueryListDownload(Pageable pageable, String checkMonth, Boolean isHr, Integer employeeType, String department, String administrativeOffice, String groups);

    HashMap<String, Object> getCrossMonthInfo(String deptName, String department, String groups, String endTime, String workCard);

    Map<String, Object> getOvertimeLeaveStatisticsQueryListNew(AcOvertimeApplicationQueryCriteria criteria, Pageable pageable, String checkMonth, Boolean isHr, Integer employeeType, String department, String administrativeOffice, String groups);

    List<HashMap<String, Object>> getMySelfOvertimeListInfoByTeam(String workCard, String deptName, String departmentName, String teamName);

    List<HashMap<String, Object>> getTeamList(String deptName, String department, String workCard);

    Integer searchDeptNumber(String deptName);

    List<HashMap<String, Object>> getDeptInfoListByDeptName(String deptName);

    Integer getUserNumberByTreeId(Integer deptId);

    List<HashMap<String, Object>> getMySelfOvertimeListInfoFormChild(String workCard, String deptName, String departmentName);

    String getUserProductionCategory(String workCard);

    List<HashMap<String, Object>> checkReviewUser2(String workCard);

    Object getOvertimeApplicationStatisticsPage(AcOvertimeApplicationQueryCriteria criteria, Pageable pageable);

    List<AcOvertimeApplicationDTO> downloadStatistics(AcOvertimeApplicationQueryCriteria criteria);
}
