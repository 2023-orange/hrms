package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcOvertimeApplication;
import com.sunten.hrms.ac.domain.OvertimeLeaveCheckList;
import com.sunten.hrms.ac.domain.ReviewFormData;
import com.sunten.hrms.ac.domain.SubOvertimeFormData;
import com.sunten.hrms.ac.dto.AcOvertimeApplicationQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zouyp
 * @since 2023-10-16
 */
@Mapper
@Repository
public interface AcOvertimeApplicationDao extends BaseMapper<AcOvertimeApplication> {

    int insertAllColumn(AcOvertimeApplication overtimeApplication);

    int deleteByKey(@Param(value = "id") Integer id);

    int deleteByEntityKey(AcOvertimeApplication overtimeApplication);

    int updateAllColumnByKey(AcOvertimeApplication overtimeApplication);

    AcOvertimeApplication getByKey(@Param(value = "id") Integer id);

    List<AcOvertimeApplication> listAllByCriteria(@Param(value = "criteria") AcOvertimeApplicationQueryCriteria criteria);

    List<AcOvertimeApplication> listAllByCriteriaPage(@Param(value = "page") Page<AcOvertimeApplication> page, @Param(value = "criteria") AcOvertimeApplicationQueryCriteria criteria);

    /**
     * 获取加班人员信息列表
     *
     * @param deptId
     * @return
     */
    List<HashMap<String, Object>> getMySelfOvertimeListInfo(@Param("deptId") Integer deptId);

    /**
     * 获取本月加班工时
     *
     * @param tempCard
     * @return
     */
    Float getOvertimeInfo(@Param("tempCard") String tempCard);

    /**
     * 从加班时数备份表获取当月加班时数
     *
     * @param deptId
     * @return
     */
    HashMap<String, Object> getDeptUsedOvertimeInfo(@Param("deptId") Integer deptId);

    /**
     * 获取本月科室已经使用的加班工时
     *
     * @param deptId
     * @return
     */
    Float getUsedMonthHours(@Param("deptId") Integer deptId);

    /**
     * 检查加班是否重复申请
     *
     * @param startTime
     * @param endTime
     * @param workCard
     * @return
     */
    HashMap checkOvertimeInfo(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("workCard") String workCard);

    /**
     * 检查加班是否重复申请
     *
     * @param startTime
     * @param endTime
     * @param workCard
     * @return
     */
    HashMap newCheckOvertimeInfo(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("workCard") String workCard);

    /**
     * 插入主表记录
     * @param param1
     * @return
     */
    int addMainTableInfo(HashMap<String, Object> param1);

    /**
     * 插入子表记录
     * @param subDataList2
     * @return
     */
    Integer addSubInfo(List<SubOvertimeFormData> subDataList2);

    /**
     * 更新加班申请当前审批记录
     * @param oaOrder
     * @param approvalNode
     * @param approvalEmployee
     * @return
     */
    int updateOaReq(@Param("oaOrder") String oaOrder, @Param("approvalNode") String approvalNode, @Param("approvalEmployee") String approvalEmployee);

    /**
     * 撤销加班申请主表记录
     * @param oaOrder
     * @return
     */
    Integer cancelOvertimeMain(@Param("oaOrder") String oaOrder);

    /**
     * 撤销加班申请子表记录
     * @param oaOrder
     * @return
     */
    Integer cancelOvertimeSub(@Param("oaOrder") String oaOrder);

    /**
     * 反写加班申请结果到主表当中
     * @param oaOrder
     * @param approvalResult
     */
    void updateResultToHrOvertime(@Param("oa_order") String oaOrder, @Param("approvalResult") String approvalResult);

    /**
     * 获取加班主表信息
     * @param checkOaOrder
     * @return
     */
    List<HashMap<String, Object>> getAcOvertimeInfo(@Param("oaOrder") String checkOaOrder);

    List<HashMap<String, Object>> getAcOvertimeSubInfo(@Param("oaOrder") String checkOaOrder);

    /**
     * 获取所有部级部门
     * @return
     */
    List<HashMap<String, Object>> getAllDepartment();

    /**
     * 获取科室
     * @param parentId
     * @return
     */
    List<HashMap<String, Object>> getDepartmentByParentId(@Param("parentId") String parentId);

    /**
     * 添加加班复核人员新
     * @param deptId
     * @param sectionId
     * @param workCard
     * @param createBy
     */
    void addOvertimeReviewUser(@Param("dept_id") Integer deptId,
                               @Param("section_id") Integer sectionId,
                               @Param("work_card") String workCard,
                               @Param("create_by") String createBy);

    /**
     * 获取全部复核人员信息List
     * @return
     */
    List<HashMap<String, Object>> getOvertimeReviewInfo();

    /**
     * 根据主键ID获取对应的复核人员信息
     * @param id
     * @return
     */
    HashMap<String, Object> getOvertimeReviewUserInfo(@Param("id") Integer id);

    /**
     * 简单查询
     * @param value
     * @return
     */
    List<HashMap<String, Object>> simpleQueryByDeptName(String value);

    /**
     * 简单查询
     * @param value
     * @return
     */
    List<HashMap<String, Object>> simpleQueryBySectionName(String value);

    /**
     * 简单查询
     * @param value
     * @return
     */
    List<HashMap<String, Object>> simpleQueryByWorCard(String value);

    /**
     * 简单查询
     * @param value
     * @return
     */
    List<HashMap<String, Object>> simpleQueryByUserName(String value);

    /**
     * 更新复核人员信息
     * @param id
     * @param deptId
     * @param sectionId
     * @param workCard
     */
    void updateOvertimeReview(@Param("id") Integer id, @Param("deptId") Integer deptId, @Param("sectionId") Integer sectionId, @Param("workCard") String workCard);

    /**
     * 删除复核人员信息
     * @param id
     */
    void deleteOvertimeReviewInfo(@Param("id") Integer id);

    /**
     * 检查是否是复核人员
     * @param workCard
     * @return
     */
    List<HashMap<String, Object>> checkReviewUser(@Param("workCard") String workCard);

    List<AcOvertimeApplication> getOvertimeApplicationReviews(Page<AcOvertimeApplication> page, AcOvertimeApplicationQueryCriteria criteria, @Param("userDepartment") String userDepartment, @Param("userSection") String userSection);

    List<AcOvertimeApplication> getOvertimeApplicationReviews2(Page<AcOvertimeApplication> page, AcOvertimeApplicationQueryCriteria criteria, @Param("userDepartment") String deptName);

    /**
     * 获取前三天后三天打卡记录
     * @param workCard
     * @param parsedDate
     * @return
     */
    List<HashMap<String, Object>> getRecentlyRecord(@Param("workCard") String workCard,@Param("parsedDate") String parsedDate);

    /**
     * 更细复核人员信息
     * @param updatedSubFormData
     */
    void updateReviewFormData(@Param("updatedSubFormData") List<ReviewFormData> updatedSubFormData);

    List<OvertimeLeaveCheckList> getOvertimeLeaveStatisticsQueryList(@Param("checkMonth") String checkMonth, @Param("pages") Integer pages, @Param("sizes") Integer sizes);

//    List<OvertimeLeaveCheckList> getOvertimeLeaveStatisticsQueryList2(@Param("checkMonth") String checkMonth,  @Param("department") String department, @Param("administrativeOffice") String administrativeOffice, @Param("pages") Integer pages, @Param("sizes") Integer sizes);
    List<OvertimeLeaveCheckList> getOvertimeLeaveStatisticsQueryList2(@Param("checkMonth") String checkMonth,  @Param("deptId") Integer deptId, @Param("administrativeOfficeId") Integer administrativeOfficeId, @Param("pages") Integer pages, @Param("sizes") Integer sizes);
    List<OvertimeLeaveCheckList> getOvertimeLeaveStatisticsQueryList3(@Param("checkMonth")String checkMonth, @Param("deptIds") Integer[] deptIds, @Param("pages") Integer pages, @Param("sizes") Integer sizes);

    List<OvertimeLeaveCheckList> getOvertimeLeaveStatisticsQueryList4(@Param("checkMonth") String checkMonth, @Param("pages") Integer pages, @Param("sizes") Integer sizes);

    List<HashMap<String, Object>> getOvertimeByCheckMonthAndWorkCard(@Param("checkMonth") String checkMonth, @Param("workCard") String workCard);

    List<HashMap<String, Object>> getLeaveByCheckMonthAndWorkCard(@Param("checkMonth") String checkMonth, @Param("workCard") String workCard);

    /**
     * 跨月申请，需要获取跨月的申请时间
     * @param deptId
     * @param endTime
     * @return
     */
    HashMap<String, Object> getCrossMonthInfo(@Param("deptId") Integer deptId, @Param("endTime") String endTime);

    Float getCrossUsedMonthHours(@Param("deptId") Integer deptId, @Param("endTime") String endTime);


    /**
     * 获取待复核列表
     * @param page
     * @param criteria
     * @param checkList
     * @return
     */


    /**
     * 获取待复核列表
     * @return
     */

    //结束时返写
    void updateByApprovalEnd(AcOvertimeApplication acOvertimeApplicationNew);

    //审批时返写
    void updateByApprovalUnderwar(AcOvertimeApplication acOvertimeApplicationNew);

    List<HashMap<String, Object>> getMySelfOvertimeListInfoByParentId(@Param("deptId") Integer deptId);

    List<HashMap<String, Object>> getMySelfOvertimeListInfoByParentId2(@Param("deptId")Integer deptId, @Param("teamName") String teamName);

    List<HashMap<String, Object>> getTeamNameList(@Param("deptId") Integer deptId);

    Integer searchDeptNumber(Integer deptId);

    List<HashMap<String, Object>> getDeptInfoListByDeptName(@Param("deptName") String deptName);

    Integer getChildNumber(@Param("dept_id") Integer dept_id);

    List<AcOvertimeApplication> getExpirationReview(Page<AcOvertimeApplication> page, AcOvertimeApplicationQueryCriteria criteria, @Param("userDepartment") String userDepartment, @Param("userSection") String userSection);

    List<AcOvertimeApplication> getExpirationReview2(Page<AcOvertimeApplication> page, AcOvertimeApplicationQueryCriteria criteria,  @Param("userDepartment") String userDepartment);

    List<AcOvertimeApplication> getReviewedList(Page<AcOvertimeApplication> page, AcOvertimeApplicationQueryCriteria criteria, @Param("userDepartment") String userDepartment, @Param("userSection") String userSection);

    List<AcOvertimeApplication> getReviewedList2(Page<AcOvertimeApplication> page, AcOvertimeApplicationQueryCriteria criteria, @Param("userDepartment") String userDepartment);

    Integer getUserNumberByTreeId(@Param("deptId") Integer deptId);

    List<HashMap<String, Object>> getMySelfOvertimeByParentId(@Param("deptId") Integer deptId);

    List<HashMap<String, Object>> getMySelfOvertimeByNDVC(Integer deptId);

    String getUserProductionCategory(@Param("workCard") String workCard);

    List<OvertimeLeaveCheckList> getOvertimeLeaveBySection(@Param("checkMonth") String checkMonth, @Param("deptId") Integer deptId, @Param("pages") Integer pages, @Param("sizes") Integer sizes);

    HashMap<String, Object> getOvertimeReviewUserInfo2(@Param("id") Integer id);

    String getSectionName(@Param("sectionId")Object sectionId);

    List<HashMap<String, Object>> getOvertimeReviewInfo2();

    List<HashMap<String, Object>> checkReviewUser2(@Param("workCard") String workCard);

    Integer getDetpId(@Param("workCard") String workCard);

    List<OvertimeLeaveCheckList> getOvertimeLeaveStatisticsQueryListTeam(@Param("checkMonth") String checkMonth, @Param("deptId") Integer deptId, @Param("administrativeOfficeId") Integer administrativeOfficeId, @Param("groupsId") Integer groupsId, @Param("pages") Integer pages, @Param("sizes") Integer sizes);

    List<OvertimeLeaveCheckList> getOvertimeLeaveStatisticsQueryListMyList(@Param("checkMonth") String checkMonth, @Param("workCard") String workCard, @Param("pages") Integer pages, @Param("sizes") Integer sizes);

    List<AcOvertimeApplication> getOvertimeApplicationStatisticsPage(@Param("page") Page<AcOvertimeApplication> page, @Param("criteria") AcOvertimeApplicationQueryCriteria criteria);

    List<AcOvertimeApplication> listAllStatisticsByCriteria(@Param(value = "criteria") AcOvertimeApplicationQueryCriteria criteria);

    ArrayList<Integer> getDeptIds(@Param("id") Long id);
}

