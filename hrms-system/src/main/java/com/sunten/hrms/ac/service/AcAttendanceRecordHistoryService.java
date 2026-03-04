package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcAttendanceRecordHistory;
import com.sunten.hrms.ac.domain.AcEmpDepts;
import com.sunten.hrms.ac.dto.AcAttendanceRecordHistoryDTO;
import com.sunten.hrms.ac.dto.AcAttendanceRecordHistoryQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>
 * 考勤处理记录历史表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
public interface AcAttendanceRecordHistoryService extends IService<AcAttendanceRecordHistory> {

    AcAttendanceRecordHistoryDTO insert(AcAttendanceRecordHistory attendanceRecordHistoryNew);

    void delete(Long id);

    void delete(AcAttendanceRecordHistory attendanceRecordHistory);

    void update(AcAttendanceRecordHistory attendanceRecordHistoryNew);

    AcAttendanceRecordHistoryDTO getByKey(Long id);

    List<AcAttendanceRecordHistoryDTO> listAll(AcAttendanceRecordHistoryQueryCriteria criteria);

    Map<String, Object> listAll(AcAttendanceRecordHistoryQueryCriteria criteria, Pageable pageable);

    void download(List<AcAttendanceRecordHistoryDTO> attendanceRecordHistoryDTOS, HttpServletResponse response) throws IOException;

    void downloadAcAdmin(List<AcAttendanceRecordHistoryDTO> attendanceRecordHistoryDTOS, HttpServletResponse response) throws IOException;

    void bartchDispose(List<AcAttendanceRecordHistory> acAttendanceRecordHistories);

    // 考勤异常批量处理前置查询
    List<AcAttendanceRecordHistoryDTO> listForBatch(AcAttendanceRecordHistoryQueryCriteria criteria);
    // 考勤异常查询分页
    Map<String, Object> listForBatchByPage(Pageable pageable ,AcAttendanceRecordHistoryQueryCriteria criteria);

//    void sendEmail();
    // 更新commitFlag
    void batchCommit(AcAttendanceRecordHistoryQueryCriteria criteria, List<AcEmpDepts> acEmpDepts, FndUserDTO user);

    void sendEmailAfterGenerateAbnormalAttendance();

    /***********************************************
     *  @author：liangjw
     *  @Date: 2021/7/12 10:17
     *  @Description: 以下为第四版开始调用的接口
     ***********************************************/
    // 考勤异常批量处理前置查询
    List<AcAttendanceRecordHistoryDTO> inUsedListForBatch(AcAttendanceRecordHistoryQueryCriteria criteria);
    // 考勤异常查询分页
    Map<String, Object> inUsedListForBatchByPage(Pageable pageable ,AcAttendanceRecordHistoryQueryCriteria criteria);

    void disposeRecordHistory(List<AcAttendanceRecordHistory> acAttendanceRecordHistories);

    // 定时更新异常处理中OA单状态为审批中的异常记录
    void autoUpdateOaCode();

    //定时更新人脸机考勤时间
    void autoUpdateAttendanceMachineTime();

    //定时同步前一天原考勤数据
    void autoSynchronousAttendanceData(String hh);

    //同步当天原考勤数据
    void autoSynchronousAttendanceNowData();
}
