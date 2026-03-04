package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcAttendanceRecordTemp;
import com.sunten.hrms.ac.dto.AcAttendanceRecordTempQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 考勤处理记录临时表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Mapper
@Repository
public interface AcAttendanceRecordTempDao extends BaseMapper<AcAttendanceRecordTemp> {

    int insertAllColumn(AcAttendanceRecordTemp attendanceRecordTemp);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcAttendanceRecordTemp attendanceRecordTemp);

    int updateAllColumnByKey(AcAttendanceRecordTemp attendanceRecordTemp);

    AcAttendanceRecordTemp getByKey(@Param(value = "id") Long id);

    List<AcAttendanceRecordTemp> listAllByCriteria(@Param(value = "criteria") AcAttendanceRecordTempQueryCriteria criteria);

    List<AcAttendanceRecordTemp> listAllByCriteriaPage(@Param(value = "page") Page<AcAttendanceRecordTemp> page, @Param(value = "criteria") AcAttendanceRecordTempQueryCriteria criteria);

    int insertTempByDept(AcAttendanceRecordTemp acAttendanceRecordTemp);

    int insertTempByEmployee(AcAttendanceRecordTemp acAttendanceRecordTemp);

    // 更新首尾两段
    int updateLastEndAndNextStart();

    // 是否休息日打卡（异常标记）
    int updateRestDayClock();

    // 是否all_day_no_clock全天未打卡
    int updateAllDayNoClock();

    // 上班时间打卡
    int updateOfficeTimeClock();

    //上班时间未打卡
    int updateNoClockIn(@Param(value = "amendHours")Long amendHours);

    // 是否abnormal_clock_in_time上下班时间打卡异常
    int updateAbnormalClockInTime();

    // 是否no_clock_after_work下班未打卡异常
    int updateNoClockAfterWork(@Param(value = "amendHours")Long amendHours);

    int insertHistoryByTemp();

    int clearTemp();

    int insertAcAbnormalClockRecord(@Param(value = "dateFrom") LocalDate dateFrom, @Param(value="dateTo") LocalDate dateTo, @Param(value = "amendHours")Long amendHours);

    int updateTimeFromByBeLate(@Param(value = "date")LocalDate date, @Param(value = "lateId") Long lateId);

    int updateTimeToByBeFore(@Param(value = "date")LocalDate date, @Param(value = "beforeId") Long beforeId);

    int updateDateClockTime(@Param(value = "beginDate")LocalDate beginDate, @Param(value = "endDate")LocalDate endDate);

    int attendanceTempToAttendanceTempArchive();

    int updateLeaveEmployee(@Param(value = "beginDate")LocalDate beginDate, @Param(value = "endDate")LocalDate endDate);
}
