package com.sunten.hrms.ac.dao;

//import com.sunten.hrms.ac.domain.AcOvertimeBackUp;
import com.sunten.hrms.ac.domain.AcOvertimeMangement;
//import com.sunten.hrms.ac.dto.AcOvertimeBackUpDTO;
import com.sunten.hrms.ac.dto.AcOvertimeManagementQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
public interface AcOvertimeMangementDao extends BaseMapper<AcOvertimeMangement> {

    int insertAllColumn(AcOvertimeMangement overtimeMangement);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcOvertimeMangement overtimeMangement);

    int updateAllColumnByKey(AcOvertimeMangement overtimeMangement);

    AcOvertimeMangement getByKey(@Param(value = "id") Long id);

    List<AcOvertimeMangement> listAllByCriteria(@Param(value = "criteria") AcOvertimeManagementQueryCriteria criteria);

    List<AcOvertimeMangement> listAllByCriteriaPage(@Param(value = "page") Page<AcOvertimeMangement> page, @Param(value = "criteria") AcOvertimeManagementQueryCriteria criteria);

    /**
     * 获取加班时数管理List
     * @return
     */
    List<HashMap<String, Object>> getOvertimeManagementList();

    /**
     * 获取加班时数管理行具体信息
     * @param id
     * @param deptId
     * @return
     */
    List<HashMap<String, Object>> getOvertimeManagementDetail(@Param("id") Integer id, @Param("deptId") Integer deptId);

    /**
     * 更新状态
     * @param id
     * @param deptId
     * @param enabledFlag
     */
    void changeAcApprovalDepartmentStatus(@Param("id") Integer id, @Param("deptId") Integer deptId, @Param("enabledFlag") Integer enabledFlag);

    /**
     * 更新详情
     * @param id
     * @param deptId
     * @param totalNumber
     * @param averageOvertimeHour
     * @param systemLimitHour
     * @param updateBy
     */
    void updateOvertimeManagementDetail(@Param("id") Integer id, @Param("dept_id") Integer deptId, @Param("total_number") Integer totalNumber,
                                        @Param("averageOvertimeHour") Float averageOvertimeHour, @Param("systemLimitHour") Float systemLimitHour,
                                        @Param("updateBy") String updateBy);

    /**
     * 新增加班工时管理部门
//     * @param id
     * @param deptId
     * @param totalNumber
     * @param averageOvertimeHour
     * @param systemLimitHour
     * @param createBy
     * @return
     */
    Integer addOvertimeManagementDetail(@Param("dept_id") Integer deptId, @Param("total_number") Integer totalNumber, @Param("averageOvertimeHour") Float averageOvertimeHour, @Param("systemLimitHour") Float systemLimitHour,
                                        @Param("createBy") String createBy);

    /**
     * 简单查询
     * @param deptName
     * @return
     */
    List<HashMap<String, Object>> checkList(@Param("deptName") String deptName);

    /**
     * 实时更新加班时数管理表的总人数
     */
    void updateTotalNumber();

    /**
     * 每月一号凌晨备份当前加班工时管理表的数据到备份表
     */
    void backupOvertimeManagementList();

    /**
     * 检查权限
     * @param workCard
     * @return
     */
    HashMap<String, Object> checkIsRole(@Param("workCard") String workCard);

    /**
     * 先看是不是主管经理类，然后获取对应部门ID那些
     * @param workCard
     * @return
     */
    HashMap<String, Object> checkUserRole(@Param("workCard") String workCard);

    /**
     * 获取主管同级别的
     * @param deptId
     * @return
     */
    List<HashMap<String, Object>> getDepartmentOvertimeList(@Param("deptId") Object deptId);

    /**
     * 获取经理管理下的部门
     * @param deptId
     * @return
     */
    List<HashMap<String, Object>> getMangerOvertimeList(@Param("deptId") Object deptId);

    /**
     * 获取每月备份List
     * @param
     * @return
     */
    List<HashMap<String, Object>> getBackupOvertimeManagementList(@Param("deptId") Integer deptId);

    /**
     * 待查询条件的备份List
     * @param
     * @param beginDate
     * @param endDate
     * @return
     */
    List<HashMap<String, Object>> getBackupOvertimeManagementListByDate(@Param("deptId") Integer deptId, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     *  主管查看本科室的
     * @param deptId
     * @param
     * @return
     */
    List<HashMap<String, Object>> getBackUpDepartmentOvertimeList(@Param("deptId") Object deptId, @Param("checkDeptId") Integer checkDeptId);

    /**
     * 经理查看管理的所有科室的
     * @param deptId
     * @param
     * @return
     */
    List<HashMap<String, Object>> getBackUpMangerOvertimeList(@Param("deptId") Object deptId, @Param("checkDeptId") Integer checkDeptId);

    /**
     * 主管查看本科室的 带日期查询
     * @param deptId
     * @param
     * @param beginDate
     * @param endDate
     * @return
     */
    List<HashMap<String, Object>> getDepartmentOvertimeListByDate(@Param("deptId") Object deptId, @Param("checkDeptId") Integer checkDeptId, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 经理查看管理的所有科室的 带日期查询
     * @param deptId
     * @param
     * @param beginDate
     * @param endDate
     * @return
     */
    List<HashMap<String, Object>> getMangerOvertimeListByDate(@Param("deptId") Object deptId, @Param("checkDeptId") Integer checkDeptId, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    void updateTotalNumberById(@Param("id") Integer id, @Param("totalNumber") int totalNumber);

    Integer getSelfNumber(@Param("deptId") Integer deptId);

    List<HashMap<String, Object>> checkList2();

    List<HashMap<String, Object>> checkList3(@Param("deptId") Integer deptId);

    List<HashMap<String, Object>> getBackupOvertimeManagementListAll();

    List<HashMap<String, Object>> getBackupOvertimeManagementListByDateAll(String beginDate, String endDate);

    List<HashMap<String, Object>> getOvertimeManagementBackupList();

    void updateTotalNumberByIdToBackup(Integer id, int i);

    /**
     * 加班时数每月备份分页查询
     * @param page
     * @param criteria
     * @return
     */
//    List<AcOvertimeBackUp> getBackOvertimeManagementListNew(Page<AcOvertimeBackUpDTO> page, AcOvertimeManagementQueryCriteria criteria);
}
