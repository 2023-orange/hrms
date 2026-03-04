package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcAttendanceMachine;
import com.sunten.hrms.ac.domain.AcAttendanceRecordHistory;
import com.sunten.hrms.ac.dto.AcAttendanceRecordHistoryQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.ac.vo.AcExceptionEmailVo;
import com.sunten.hrms.ac.vo.RepulseEmailMes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 考勤处理记录历史表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Mapper
@Repository
public interface AcAttendanceRecordHistoryDao extends BaseMapper<AcAttendanceRecordHistory> {

    int insertAllColumn(AcAttendanceRecordHistory attendanceRecordHistory);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcAttendanceRecordHistory attendanceRecordHistory);

    int updateAllColumnByKey(AcAttendanceRecordHistory attendanceRecordHistory);

    AcAttendanceRecordHistory getByKey(@Param(value = "id") Long id);

    List<AcAttendanceRecordHistory> listAllByCriteria(@Param(value = "criteria") AcAttendanceRecordHistoryQueryCriteria criteria);

    List<AcAttendanceRecordHistory> listAllByCriteriaPage(@Param(value = "page") Page<AcAttendanceRecordHistory> page, @Param(value = "criteria") AcAttendanceRecordHistoryQueryCriteria criteria);

    int updateDisposeFlagByKey(AcAttendanceRecordHistory attendanceRecordHistory);

    Integer countHistory(@Param(value = "criteria") AcAttendanceRecordHistoryQueryCriteria criteria);

    // 批量操作前置的查询
    List<AcAttendanceRecordHistory> listForBatch(@Param(value = "criteria") AcAttendanceRecordHistoryQueryCriteria criteria);
    // 考勤异常查询分页
    List<AcAttendanceRecordHistory> listForBatchByPage(@Param(value = "page") Page<AcAttendanceRecordHistory> page, @Param(value = "criteria") AcAttendanceRecordHistoryQueryCriteria criteria);

   Integer countForDocCommit(@Param(value = "criteria") AcAttendanceRecordHistoryQueryCriteria criteria);

   int updateBatchCommit(@Param(value = "criteria") AcAttendanceRecordHistoryQueryCriteria criteria);

   int updateBatchDataTest(@Param(value = "criteria") AcAttendanceRecordHistoryQueryCriteria criteria);

   // 打回到领导、个人
   List<RepulseEmailMes> getPersonEmailInfoByAccrId(@Param(value = "criteria") AcAttendanceRecordHistoryQueryCriteria criteria);

   // 打回到资料员
   List<RepulseEmailMes> getDocEmailInfoByAccrId(@Param(value = "criteria") AcAttendanceRecordHistoryQueryCriteria criteria);

   // 异常生成后的邮件发送
   List<AcExceptionEmailVo> listEmailTargetByLastMonthException();

    /***********************************************
     *  @author：liangjw
     *  @Date: 2021/7/12 10:17
     *  @Description: 以下为第四版开始调用的接口
     ***********************************************/
    // 待办列表异常数量数数专用
    Integer inUsedCountHistory(@Param(value = "criteria") AcAttendanceRecordHistoryQueryCriteria criteria);
    // 批量操作前置的查询
    List<AcAttendanceRecordHistory> inUsedListForBatch(@Param(value = "criteria") AcAttendanceRecordHistoryQueryCriteria criteria);
    // 考勤异常查询分页
    List<AcAttendanceRecordHistory> inUsedListForBatchByPage(@Param(value = "page") Page<AcAttendanceRecordHistory> page, @Param(value = "criteria") AcAttendanceRecordHistoryQueryCriteria criteria);
    // 批量提交上月异常时的数数
    Integer inUsedCountForDocCommit(@Param(value = "criteria") AcAttendanceRecordHistoryQueryCriteria criteria);
    // 批量更新上月异常的提交标记
    int inUsedUpdateBatchCommit(@Param(value = "criteria") AcAttendanceRecordHistoryQueryCriteria criteria);

    int inUsedUpdateByBatchDisposed(AcAttendanceRecordHistory acAttendanceRecordHistory);

    int inUsedUpdateDisposeByColumn(AcAttendanceRecordHistory acAttendanceRecordHistory);
    // 获取审批中状态的异常记录
    List<AcAttendanceRecordHistory> listByWaitState();

    // 修改
    int updateStateByReqCode(AcAttendanceRecordHistory acAttendanceRecordHistory);

    //获取人脸机IP地址
    List<AcAttendanceMachine> getMachineListAll();

    void insertAttendanceData(@Param(value = "name")String name,
                              @Param(value = "date8")String date8,
                              @Param(value = "time8")String time8,
                              @Param(value = "crjqk")String crjqk,
                              @Param(value = "machineName")String machineName);
}
