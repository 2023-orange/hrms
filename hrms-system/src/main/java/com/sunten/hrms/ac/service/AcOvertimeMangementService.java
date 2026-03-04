package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcOvertimeMangement;
import com.sunten.hrms.ac.dto.AcOvertimeMangementDTO;
import com.sunten.hrms.ac.dto.AcOvertimeManagementQueryCriteria;
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
public interface AcOvertimeMangementService extends IService<AcOvertimeMangement> {

    AcOvertimeMangementDTO insert(AcOvertimeMangement overtimeMangementNew);

    void delete(Long id);

    void delete(AcOvertimeMangement overtimeMangement);

    void update(AcOvertimeMangement overtimeMangementNew);

    AcOvertimeMangementDTO getByKey(Long id);

    List<AcOvertimeMangementDTO> listAll(AcOvertimeManagementQueryCriteria criteria);

    Map<String, Object> listAll(AcOvertimeManagementQueryCriteria criteria, Pageable pageable);

    void download(List<AcOvertimeMangementDTO> overtimeMangementDTOS, HttpServletResponse response) throws IOException;

    /**
     * 获取加班时数管理页面
     * @return
     */
    List<HashMap<String, Object>> getOvertimeManagementList();

    /**
     * 获取加班时数管理行具体信息
     * @param id
     * @param deptId
     * @return
     */
    List<HashMap<String, Object>> getOvertimeManagementDetail(Integer id, Integer deptId);

    /**
     * 更新状态
     * @param id
     * @param deptId
     * @param i
     */
    void changeAcApprovalDepartmentStatus(Integer id, Integer deptId, int i);

    /**
     * 更新加班时数管理详情
     * @param id
     * @param deptId
     * @param totalNumber
     * @param averageOvertimeHour
     * @param systemLimitHour
     * @param updateBy
     */
    void updateOvertimeManagementDetail(Integer id, Integer deptId, Integer totalNumber, Float averageOvertimeHour, Float systemLimitHour, String updateBy);

    /**
     * 新增部门加班工时管理
     * @param dept_id
     * @param deptName
     * @param totalNumber
     * @param averageOvertimeHour
     * @param systemLimitHour
     * @param createBy
     * @return
     */
    Integer addOvertimeManagementDetail(Integer dept_id, String deptName, Integer totalNumber, Float averageOvertimeHour, Float systemLimitHour, String createBy);

    /**
     * 简单查询
     * @param
     * @return
     */
    List<HashMap<String, Object>> checkList(Integer deptId);

    /**
     * 每月1号备份一次
     */
    void backupOvertimeManagementList();

    /**
     * 检查权限
     * @param workCard
     * @return
     */
    HashMap<String, Object> checkIsRole(String workCard);

    /**
     * 主管经理级别获取自己管理的科室列表
     * @param workCard
     * @return
     */
    List<HashMap<String, Object>> getAndCheckList(String workCard);

    /**
     * 仅根据部门名称来查询
     * @param deptName
     * @return
     */
    List<HashMap<String, Object>> getBackOvertimeManagementListByDeptName(Integer deptName);

    /**
     * 根据部门名称和时间段来筛选
     * @param
     * @param beginDate
     * @param endDate
     * @return
     */
    List<HashMap<String, Object>> acOvertimeManagementServiceByDate(Integer deptId, String beginDate, String endDate);

    /**
     * 主管经理级获取管理部门下的信息
     * @param
     * @param workCard
     * @return
     */
    List<HashMap<String, Object>> getBackUpAndCheckListByDeptName(Integer deptId, String workCard);

    /**
     * 主管经理级获取管理部门下的信息
     * @param
     * @param beginDate
     * @param endDate
     * @param workCard
     * @return
     */
    List<HashMap<String, Object>> getBackUpAndCheckListByDate(Integer deptId, String beginDate, String endDate, String workCard);

//    Map<String, Object> getBackOvertimeManagementListNew(AcOvertimeManagementQueryCriteria criteria, Pageable pageable);
}
