package com.sunten.hrms.ac.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mchange.lang.LongUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sunten.hrms.ac.dao.*;
import com.sunten.hrms.ac.domain.*;
import com.sunten.hrms.ac.dto.AcAbnormalAttendanceRecordDTO;
import com.sunten.hrms.ac.dto.AcAttendanceRecordTempQueryCriteria;
import com.sunten.hrms.ac.dto.AcBeLateDateQueryCriteria;
import com.sunten.hrms.ac.dto.AcDeptAttendanceQueryCriteria;
import com.sunten.hrms.ac.service.AcAbnormalAttendanceRecordService;
import com.sunten.hrms.ac.service.AcLeaveFormTempService;
import com.sunten.hrms.ac.util.AcUtil;
import com.sunten.hrms.ac.vo.AcRegimeTimeVo;
import com.sunten.hrms.fnd.dao.FndDeptSnapshotDao;
import com.sunten.hrms.fnd.domain.FndDeptSnapshot;
import com.sunten.hrms.fnd.dto.FndDeptSnapshotQueryCriteria;
import com.sunten.hrms.utils.DateUtil;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* AcAttendanceRecordTempServiceImpl Tester.
*
* @author <Authors name>
* @since <pre>10/15/2020</pre>
* @version 1.0
*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class AcAttendanceRecordTempServiceImplTest {
    @Autowired
    FndDeptSnapshotDao fndDeptSnapshotDao;
    @Autowired
    AcDeptAttendanceDao acDeptAttendanceDao;

    @Autowired
    AcAbnormalAttendanceRecordDao acAbnormalAttendanceRecordDao;
    @Autowired
    AcAttendanceRecordTempDao acAttendanceRecordTempDao;
    @Autowired
    AcAttendanceRecordTempServiceImpl acAttendanceRecordTempServiceImpl;

    @Autowired
    AcAbnormalAttendanceRecordService acAbnormalAttendanceRecordService;
    @Autowired
    AcLeaveFormTempDao acLeaveFormTempDao;

    @Autowired
    AcSetUpDao acSetUpDao;

    @Autowired
    AcBeLateDateDao acBeLateDateDao;

    @Autowired
    AcLeaveFormTempService acLeaveFormTempService;

@Before
public void before() throws Exception {
}

@After
public void after() throws Exception {
}

@Test
public void deptEmpAttendance() {

}

// 补生成指定日期的异常记录，要确保ac_attendance_record_history没有这天的记录，且ac_attendance_record_tmp为空
@Test
public void testAdd(){
    LocalDate startDate = LocalDate.of(2025,2,19);
    LocalDate endDate = LocalDate.of(2025,2,19);
    AcAttendanceRecordTemp insertTemp = new AcAttendanceRecordTemp();
    insertTemp.setStartDate(startDate);
    insertTemp.setEndDate(endDate);
    insertTemp.setRecordId(70L);  // 指定生成异常的记录头ID
    insertTemp.setCreateTime(LocalDateTime.now());
    insertTemp.setUpdateTime(LocalDateTime.now());
    insertTemp.setUpdateBy(-1L);
    insertTemp.setCreateBy(-1L);
    acAttendanceRecordTempDao.insertTempByEmployee(insertTemp);
    acAttendanceRecordTempDao.insertTempByDept(insertTemp);
    // 获取请假条
    List<AcLeaveFormTemp> acLeaveFormTemps = acAttendanceRecordTempServiceImpl.getLeaveFromTempByDate(startDate, endDate);
    // 对请假条进行匹配
    this.matchLeaveFrom(acLeaveFormTemps);
    // 获取厂车迟到, 进行匹配
    this.matchBeLate(startDate, endDate);
    if (!true) { // 不以 0 点为截断
        // 更新前一段的结束时间与后一段的开始时间
        acAttendanceRecordTempDao.updateLastEndAndNextStart();
    }
    // matchException
    acAttendanceRecordTempDao.updateRestDayClock();
    acAttendanceRecordTempDao.updateAllDayNoClock();
    acAttendanceRecordTempDao.updateOfficeTimeClock();
    acAttendanceRecordTempDao.updateNoClockIn(2L);
    acAttendanceRecordTempDao.updateAbnormalClockInTime();
    acAttendanceRecordTempDao.updateNoClockAfterWork(2L);
    acAttendanceRecordTempDao.updateNoClockAfterWork(2L);
    // temp turn  history
    acAttendanceRecordTempDao.insertHistoryByTemp();
    acAttendanceRecordTempDao.clearTemp();
    acAttendanceRecordTempDao.insertAcAbnormalClockRecord(startDate, endDate, 2L);
    // 更新aarh上的dayClockTime字段
    acAttendanceRecordTempDao.updateDateClockTime(startDate, endDate);
    acAttendanceRecordTempDao.updateLeaveEmployee(startDate, endDate);

}



@Test
@Transactional(rollbackFor = Exception.class)
public void testNewTrainsation() {
    AcAbnormalAttendanceRecord acAbnormalAttendanceRecord = new AcAbnormalAttendanceRecord();
    acAbnormalAttendanceRecord.setExecutionStatus("T");
    acAbnormalAttendanceRecord.setStartTime(LocalDate.now());
    acAbnormalAttendanceRecord.setEndTime(LocalDate.now());
    acAbnormalAttendanceRecord.setExecutionTime(LocalDateTime.now());
    acAbnormalAttendanceRecordService.insertByNewTransation(acAbnormalAttendanceRecord);
    AcLeaveFormTemp acLeaveForm = new AcLeaveFormTemp();
    acLeaveForm.setWorkCard("1111");
    acLeaveForm.setStartTime(LocalDateTime.now());
    acLeaveForm.setEndTime(LocalDateTime.now());
    acLeaveFormTempDao.insertAllColumn(acLeaveForm);

    AcLeaveFormTemp acLeaveFormTemp = new AcLeaveFormTemp();
    acLeaveForm.setStartTime(LocalDateTime.now());
    acLeaveForm.setEndTime(LocalDateTime.now());
    acLeaveFormTempDao.insertAllColumn(acLeaveFormTemp);

}

@Test
@Transactional(rollbackFor = Exception.class)
public void testTrainsationPoint() {
    AcAbnormalAttendanceRecord acAbnormalAttendanceRecord = new AcAbnormalAttendanceRecord();
    Object savePoint = null;        acAbnormalAttendanceRecord.setExecutionStatus("Test");
    acAbnormalAttendanceRecord.setStartTime(LocalDate.now());
    acAbnormalAttendanceRecord.setEndTime(LocalDate.now());
    acAbnormalAttendanceRecord.setExecutionTime(LocalDateTime.now());
    acAbnormalAttendanceRecordDao.insert(acAbnormalAttendanceRecord);

    try {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        savePoint =  TransactionAspectSupport.currentTransactionStatus().createSavepoint();

        AcLeaveFormTemp acLeaveForm = new AcLeaveFormTemp();
        acLeaveForm.setWorkCard("444xxxxxxx4");
        acLeaveForm.setStartTime(LocalDateTime.now());
        acLeaveForm.setEndTime(LocalDateTime.now());
        acLeaveFormTempDao.insert(acLeaveForm);

        AcLeaveFormTemp acLeaveFormTemp = new AcLeaveFormTemp();
        acLeaveFormTemp.setStartTime(LocalDateTime.now());
        acLeaveFormTemp.setEndTime(LocalDateTime.now());
        acLeaveFormTempDao.insert(acLeaveFormTemp);
    }catch (Exception e ) {
        e.printStackTrace();
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                if(null != savePoint) {
//            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
//        }
        acAbnormalAttendanceRecord.setExecutionStatus("ERRORtest");
        acAbnormalAttendanceRecordService.update(acAbnormalAttendanceRecord);
    }

}

/**
*
* Method: insert(AcAttendanceRecordTemp attendanceRecordTempNew)
*
*/
@Test
public void testInsert() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: delete(Long id)
*
*/
@Test
public void testDeleteId() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: delete(AcAttendanceRecordTemp attendanceRecordTemp)
*
*/
@Test
public void testDeleteAttendanceRecordTemp() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: update(AcAttendanceRecordTemp attendanceRecordTempNew)
*
*/
@Test
public void testUpdate() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: getByKey(Long id)
*
*/
@Test
public void testGetByKey() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: listAll(AcAttendanceRecordTempQueryCriteria criteria)
*
*/
@Test
public void testListAllCriteria() throws Exception {
//TODO: Test goes here...
    AcAttendanceRecordTempQueryCriteria acAttendanceRecordTempQueryCriteria = new AcAttendanceRecordTempQueryCriteria();
    acAttendanceRecordTempQueryCriteria.setParentId(1L);
    acAttendanceRecordTempQueryCriteria.setEmployeeId(183L);
    acAttendanceRecordTempQueryCriteria.setStartDate(LocalDate.of(2020,10,15));
    acAttendanceRecordTempQueryCriteria.setEndDate(LocalDate.of(2020,10,16));
    List<AcAttendanceRecordTemp> acAttendanceRecordTemps = acAttendanceRecordTempDao.listAllByCriteria(acAttendanceRecordTempQueryCriteria);
    for (AcAttendanceRecordTemp ac: acAttendanceRecordTemps
         ) {
        System.out.println(ac);
    }
}

/**
*
* Method: listAll(AcAttendanceRecordTempQueryCriteria criteria, Pageable pageable)
*
*/
@Test
public void testListAllForCriteriaPageable() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: download(List<AcAttendanceRecordTempDTO> attendanceRecordTempDTOS, HttpServletResponse response)
*
*/




        @Test
        public void setAcUpSet() {
            AcSetUp acSetUp = new AcSetUp();
            acSetUp.setStageFlag(false);
            acSetUp.setInterval(10);
            acSetUp.setRunTime(LocalTime.of(0,0,0));
            acSetUpDao.insertAllColumn(acSetUp);
        }

        @Test
        public void testMatchAcLeaveForm() {
// 插入一条执行记录
            AcAbnormalAttendanceRecord addRecord = new AcAbnormalAttendanceRecord();
            AcSetUp acSetUp = acSetUpDao.getByNew();
            // 先获取上一次执行成功的出异常的结束日期
            AcAbnormalAttendanceRecord acAbnormalAttendanceRecord = acAbnormalAttendanceRecordDao.getLastOne();
            // 开始日期
            LocalDate startDate;
            if (null != acAbnormalAttendanceRecord) { // 有上一次执行
                // 日期+1
                startDate = acAbnormalAttendanceRecord.getEndTime().plusDays(1);
                addRecord.setStartTime(startDate);
            } else {
                startDate = LocalDate.of(2020,1,1);
                addRecord.setStartTime(LocalDate.of(2020,1,1));
            }
            // 结束日期为今天减2日
//            LocalDate endDate = LocalDate.now().minusDays(2);
            LocalDate endDate = LocalDate.of(2021,6, 30);
            addRecord.setEndTime(endDate);
            addRecord.setExecutionStatus("SUCCESS");
            addRecord.setExecutionTime(LocalDateTime.now());
            int addRecordId = acAbnormalAttendanceRecordDao.insertAllColumn(addRecord);
            // 生成该时间段的初始排班
            AcAttendanceRecordTemp insertTemp = new AcAttendanceRecordTemp();
            insertTemp.setStartDate(startDate);
            insertTemp.setEndDate(endDate);
            insertTemp.setRecordId((long) addRecordId);
            insertTemp.setCreateTime(LocalDateTime.now());
            insertTemp.setUpdateTime(LocalDateTime.now());
            insertTemp.setUpdateBy(-1L);
            insertTemp.setCreateBy(-1L);
            acAttendanceRecordTempDao.insertTempByEmployee(insertTemp);
            acAttendanceRecordTempDao.insertTempByDept(insertTemp);
//            // 重新拿出刚才插入的初始排班
//            AcAttendanceRecordTempQueryCriteria acAttendanceRecordTempQueryCriteria = new AcAttendanceRecordTempQueryCriteria();
//            acAttendanceRecordTempQueryCriteria.setParentId((long) addRecordId);
//            List<AcAttendanceRecordTemp> acAttendanceRecordTemps = acAttendanceRecordTempDao.listAllByCriteria(acAttendanceRecordTempQueryCriteria);
            // 获取请假条
//            List<AcLeaveFormTemp> acLeaveFormTemps = acAttendanceRecordTempServiceImpl.getLeaveFromTempByDate(startDate,endDate);
//            // 对请假条进行匹配
//            acAttendanceRecordTempServiceImpl.matchLeaveFrom(acLeaveFormTemps);
//            if (!acSetUp.getStageFlag()) { // 不以 0 点为截断
//                // 更新前一段的结束时间与后一段的开始时间
//                acAttendanceRecordTempDao.updateLastEndAndNextStart();
//            }
            // 异常匹配

        }
    @Test
    public void testMatch() {

        List<AcLeaveFormTemp> acLeaveFormTemps = acAttendanceRecordTempServiceImpl.getLeaveFromTempByDate(LocalDate.of(2020,10,1),LocalDate.now());
        // 对请假条进行匹配
        acAttendanceRecordTempServiceImpl.matchLeaveFrom(acLeaveFormTemps);
    }


    @Test
    public void testUpdateRestDayClock() {
            acAttendanceRecordTempDao.updateRestDayClock();
    }

    @Test
    public void updateAllDayNoClock() {
            acAttendanceRecordTempDao.updateAllDayNoClock();
    }

    @Test
    public void updateOfficeTimeClock() {
            acAttendanceRecordTempDao.updateOfficeTimeClock();
    }

    @Test
    public void updateNoClockIn() {
            acAttendanceRecordTempDao.updateNoClockIn(2L);
    }

    @Test
    public void updateNoClockAfterWork() {
            acAttendanceRecordTempDao.updateNoClockAfterWork(2L);
    }

    @Test
    public void updateAbnormalClockInTime() {
            acAttendanceRecordTempDao.updateAbnormalClockInTime();
    }

    @Test
    public void insertHistoryByTemp() {
            acAttendanceRecordTempDao.insertHistoryByTemp();
    }

    @Test
    public void insertAcAbnormalClockRecord() {
            acAttendanceRecordTempDao.insertAcAbnormalClockRecord(LocalDate.of(2020,1,1),LocalDate.now().minusDays(2), 2L);
    }

    public Long[] getFromTo(LocalDateTime fromTime, LocalDateTime toTime,LocalDateTime startTime,LocalDateTime endTime){
        Long fromTimeL = DateUtil.asLong(fromTime);
        Long toTimeL = DateUtil.asLong(toTime);
        Long startTimeL = DateUtil.asLong(startTime);
        Long endTimeL = DateUtil.asLong(endTime);
        Long[] longs;
        if ((fromTimeL >= endTimeL && toTimeL >= endTimeL) || (fromTimeL <= startTimeL && toTimeL <=startTimeL)
        ) {
            longs = new Long[2];
            longs[0] = fromTimeL;
            longs[1] = toTimeL;
            return longs;
        }
        if (fromTimeL <= endTimeL && fromTimeL >= startTimeL && toTimeL <= endTimeL && toTimeL >= startTimeL) {
            return null;
        }
        if (fromTimeL < startTimeL && toTimeL < endTimeL) { // 请假的开始时间在段内
            longs = new Long[2];
            longs[0] = fromTimeL;
            longs[1] = startTimeL;
            return longs;
        }
        if (fromTimeL > startTimeL && fromTimeL < endTimeL && toTimeL > endTimeL) { // 请假的结束时间在段内
            longs = new Long[2];
            longs[0] = endTimeL;
            longs[1] = toTimeL;
            return longs;
        }
        if (startTimeL > fromTimeL && endTimeL < toTimeL) {
            longs = new Long[4];
            longs[0] = fromTimeL;
            longs[1] = startTimeL;
            longs[2] = endTimeL;
            longs[3] = toTimeL;
            return longs;
        }
        return null;
    }


    public void matchLeaveFrom(List<AcLeaveFormTemp> acLeaveFormTemps) {
        AcAttendanceRecordTempQueryCriteria acAttendanceRecordTempQueryCriteria;
        for (AcLeaveFormTemp acLeaveFormTemp : acLeaveFormTemps) {
            // 查询该员工在该段请假时间内的且不为休息日的排班
            acAttendanceRecordTempQueryCriteria = new AcAttendanceRecordTempQueryCriteria();
            acAttendanceRecordTempQueryCriteria.setEmployeeId(acLeaveFormTemp.getPmEmployee().getId());
            acAttendanceRecordTempQueryCriteria.setStartDate(acLeaveFormTemp.getStartTime().toLocalDate());
            acAttendanceRecordTempQueryCriteria.setEndDate(acLeaveFormTemp.getEndTime().toLocalDate());
            acAttendanceRecordTempQueryCriteria.setRestFlag(false);
            List<AcAttendanceRecordTemp> acAttendanceRecordTemps = acAttendanceRecordTempDao.listAllByCriteria(acAttendanceRecordTempQueryCriteria);
            for (AcAttendanceRecordTemp acAttendacneTemp : acAttendanceRecordTemps) {
                if(acAttendacneTemp.getDate().equals(acLeaveFormTemp.getStartTime().toLocalDate()) || acAttendacneTemp.getDate().equals(acLeaveFormTemp.getEndTime().toLocalDate())){
                    // 工作时间段与请假时间段有交集, 将三段放入集合循环
                    List<AcRegimeTimeVo> acRegimeTimeVos = AcUtil.transferAttendanceAltTimeToTimeList(acAttendacneTemp, false);
                    List<AcRegimeTimeVo> timeVosMatchResult = new ArrayList<>();
                    for(AcRegimeTimeVo vo:acRegimeTimeVos){
                        LocalDateTime timeFrom = acAttendacneTemp.getDate().atTime(vo.getTimeFrom());
                        LocalDateTime timeTo=  acAttendacneTemp.getDate().atTime(vo.getTimeTo());
                        // 跨日 + 1 日
                        if(vo.getExtendTimeFlag()){
                            timeTo = timeTo.plusDays(1);
                        }
                        LocalDateTime leaveFrom= acLeaveFormTemp.getStartTime();
                        LocalDateTime leaveTo= acLeaveFormTemp.getEndTime();
                        // 处理完后的时间段
                        List<LocalDateTime> times = AcUtil.altTimeByLeave(timeFrom, timeTo, leaveFrom, leaveTo);
                        if(times != null){
                            if(times.size() == 2 || times.size() == 4){
                                // 有两段时取 ，0 1
                                timeVosMatchResult.add(AcUtil.createAcRegimeTimeVo(acAttendacneTemp, times, 0, 1));
                                if(times.size() == 4){
                                    // 有四段时， 取 0 1 2 3
                                    timeVosMatchResult.add(AcUtil.createAcRegimeTimeVo(acAttendacneTemp, times, 2, 3));
                                }
                            }
                        }
                    }
                    AcRegimeTimeVo timeVo = new AcRegimeTimeVo();
                    AcUtil.setFirstTime(acAttendacneTemp, timeVo);
                    AcUtil.setSecondTime(acAttendacneTemp, timeVo);
                    AcUtil.setThirdTime(acAttendacneTemp, timeVo);
                    AcUtil.setTimeListToAttendanceAltTime(timeVosMatchResult, acAttendacneTemp, false);
                }else{
                    AcUtil.setAttendanceRestDay(acAttendacneTemp);
                }
                // TODO 更新
                acAttendanceRecordTempDao.updateAllColumnByKey(acAttendacneTemp);

            }


        }


    }

    public List<AcAttendanceRecordTemp> matchLeaveForm(List<AcLeaveFormTemp> acLeaveFormTemps, List<AcAttendanceRecordTemp> acAttendanceRecordTemps) {
    Long[] results;
    for (AcLeaveFormTemp acLeaveFormTemp: acLeaveFormTemps
             ) {
            for (AcAttendanceRecordTemp acAttendacneTemp : acAttendanceRecordTemps
                 ) {
                Boolean firstFlag = false;
                Boolean thirdFlag = false;
                Boolean secondFlag = false;
                if (acLeaveFormTemp.getPmEmployee().getId().equals(acAttendacneTemp.getEmployeeId()) && !acAttendacneTemp.getRestFlag()
                        && ((acAttendacneTemp.getDate().isBefore(acLeaveFormTemp.getEndTime().toLocalDate()) && acAttendacneTemp.getDate().isAfter(acLeaveFormTemp.getStartTime().toLocalDate()))
                        || acAttendacneTemp.getDate().equals(acLeaveFormTemp.getEndTime().toLocalDate()) || acAttendacneTemp.getDate().equals(acLeaveFormTemp.getStartTime().toLocalDate()))) {
                    // 请假的开始日期早于今日的
                    if (null != acAttendacneTemp.getThirdTimeFrom() && null != acAttendacneTemp.getThirdTimeTo()) {
                        // 开始处理第三段
                        results = this.getFromTo(acAttendacneTemp.getDate().atTime(acAttendacneTemp.getThirdTimeFrom()),
                                acAttendacneTemp.getDate().plusDays(acAttendacneTemp.getExtend3TimeFlag() ? 1 : 0).atTime(acAttendacneTemp.getThirdTimeTo()),
                                acLeaveFormTemp.getStartTime(), acLeaveFormTemp.getEndTime());
                        if (null == results || results.length == 0) {
                            acAttendacneTemp.setAltThirdTimeFrom(null);
                            acAttendacneTemp.setAltThirdTimeTo(null);
                            acAttendacneTemp.setAltExtend3TimeFlag(null);
                        } else if (results.length == 2) {
                            setThirdTime(results, acAttendacneTemp, 0, 1);
                            thirdFlag = true;
                        } else {
                            // 异常,四段
                            acAttendacneTemp.setNoScheduling(true);
                            continue;
                        }
                    }
                    if (null != acAttendacneTemp.getSecondTimeFrom() && null != acAttendacneTemp.getSecondTimeTo()) {
                        // 开始处理第二段
                        results = this.getFromTo(acAttendacneTemp.getDate().atTime(acAttendacneTemp.getSecondTimeFrom()),
                                acAttendacneTemp.getDate().atTime(acAttendacneTemp.getSecondTimeTo()),
                                acLeaveFormTemp.getStartTime(), acLeaveFormTemp.getEndTime());
                        if (null == results || results.length == 0) {
                            setSecondTime(acAttendacneTemp, null, null, null);
                        } else if (results.length == 2) {
                            setSecondTime(results, acAttendacneTemp, 0, 1);
                            secondFlag = true;
                        } else {
                            secondFlag = true;
                            if (thirdFlag) { // 有第三段
                                // 异常四段
                                acAttendacneTemp.setNoScheduling(true);
                                continue;
                            } else { // 无第三段
                                // 2段分 2 3
                                setSecondTime(results, acAttendacneTemp, 0, 1);
                                setThirdTime(results, acAttendacneTemp, 2, 3);
                            }
                        }
                    }

                    if (null != acAttendacneTemp.getFirstTimeFrom() && null != acAttendacneTemp.getFirstTimeTo()){
                        // 开始处理第一段
                        results = this.getFromTo(acAttendacneTemp.getDate().atTime(acAttendacneTemp.getFirstTimeFrom()),
                                acAttendacneTemp.getDate().atTime(acAttendacneTemp.getFirstTimeTo()),
                                acLeaveFormTemp.getStartTime(), acLeaveFormTemp.getEndTime());
                        if (null == results || results.length == 0) {
                            setFirstTime(acAttendacneTemp, null, null, null);
                        } else if (results.length == 2) {
                            firstFlag = true;
                            setFirstTime(results, acAttendacneTemp);
                        } else {
                            firstFlag = true;
                            if (secondFlag && thirdFlag) {
                                acAttendacneTemp.setNoScheduling(true);
                                continue;
                            } else {
                                setFirstTime(results, acAttendacneTemp);
                                if(!secondFlag){
                                    setSecondTime(results, acAttendacneTemp, 2, 3);
                                } else {
                                    setThirdTime(results, acAttendacneTemp, 2, 3);
                                }
                            }

                        }
                    }
                    if(!firstFlag || !secondFlag){
                        List<AltTime> acAttendanceRecords = new ArrayList<>();
                        if(firstFlag){
                            AltTime altTime = new AltTime();
                            altTime.setTimeFrom(acAttendacneTemp.getAltFirstTimeFrom());
                            altTime.setTimeTo(acAttendacneTemp.getAltFirstTimeTo());
                            altTime.setExtendTimeFlag(acAttendacneTemp.getAltExtend1TimeFlag());
                            acAttendanceRecords.add(altTime);
                        }
                        if(secondFlag){
                            AltTime altTime = new AltTime();
                            altTime.setTimeFrom(acAttendacneTemp.getAltSecondTimeFrom());
                            altTime.setTimeTo(acAttendacneTemp.getAltSecondTimeTo());
                            altTime.setExtendTimeFlag(acAttendacneTemp.getAltExtend2TimeFlag());
                            acAttendanceRecords.add(altTime);
                        }
                        if(thirdFlag){
                            AltTime altTime = new AltTime();
                            altTime.setTimeFrom(acAttendacneTemp.getAltThirdTimeFrom());
                            altTime.setTimeTo(acAttendacneTemp.getAltThirdTimeTo());
                            altTime.setExtendTimeFlag(acAttendacneTemp.getAltExtend3TimeFlag());
                            acAttendanceRecords.add(altTime);
                        }
                        setFirstTime(acAttendacneTemp, null, null, null);
                        setSecondTime(acAttendacneTemp, null, null, null);
                        acAttendacneTemp.setAltThirdTimeFrom(null);
                        acAttendacneTemp.setAltThirdTimeTo(null);
                        acAttendacneTemp.setAltExtend3TimeFlag(null);
                        if(acAttendanceRecords.size()>0){
                            AltTime altTime = acAttendanceRecords.get(0);
                            setFirstTime(acAttendacneTemp, altTime.getTimeFrom(), altTime.getTimeTo(), altTime.getExtendTimeFlag());
                        }
                        if(acAttendanceRecords.size()>1){
                            AltTime altTime = acAttendanceRecords.get(1);
                            setSecondTime(acAttendacneTemp, altTime.getTimeFrom(), altTime.getTimeTo(), altTime.getExtendTimeFlag());
                        }
                    }
                }
            }
        }
    return acAttendanceRecordTemps;
    }

    public void setFirstTime(AcAttendanceRecordTemp acAttendacneTemp, LocalTime timeFrom, LocalTime timeTo, Boolean extendTimeFlag) {
        acAttendacneTemp.setAltFirstTimeFrom(timeFrom);
        acAttendacneTemp.setAltFirstTimeTo(timeTo);
        acAttendacneTemp.setAltExtend1TimeFlag(extendTimeFlag);
    }

    public void setSecondTime(AcAttendanceRecordTemp acAttendacneTemp, LocalTime timeFrom, LocalTime timeTo, Boolean extendTimeFlag) {
        acAttendacneTemp.setAltSecondTimeFrom(timeFrom);
        acAttendacneTemp.setAltSecondTimeTo(timeTo);
        acAttendacneTemp.setAltExtend2TimeFlag(extendTimeFlag);
    }

    class AltTime{
        private LocalTime timeFrom;
        private LocalTime timeTo;
        private Boolean extendTimeFlag;

        public LocalTime getTimeFrom() {
            return timeFrom;
        }

        public void setTimeFrom(LocalTime timeFrom) {
            this.timeFrom = timeFrom;
        }

        public LocalTime getTimeTo() {
            return timeTo;
        }

        public void setTimeTo(LocalTime timeTo) {
            this.timeTo = timeTo;
        }

        public Boolean getExtendTimeFlag() {
            return extendTimeFlag;
        }

        public void setExtendTimeFlag(Boolean extendTimeFlag) {
            this.extendTimeFlag = extendTimeFlag;
        }
    }

     public void setFirstTime(Long[] results, AcAttendanceRecordTemp acAttendacneTemp) {
        acAttendacneTemp.setAltFirstTimeFrom(DateUtil.asLocalDateTime(new Timestamp(results[0])).toLocalTime());
        acAttendacneTemp.setAltFirstTimeTo(DateUtil.asLocalDateTime(new Timestamp(results[1])).toLocalTime());
        if (DateUtil.asLocalDateTime(new Timestamp(results[1])).toLocalDate().isAfter(acAttendacneTemp.getDate())
        ) { // 计算出的时间是否晚于考勤日的日期
            acAttendacneTemp.setAltExtend1TimeFlag(true);
        } else {
            acAttendacneTemp.setAltExtend1TimeFlag(false);
        }
    }

    public void setSecondTime(Long[] results, AcAttendanceRecordTemp acAttendacneTemp, int i, int i2) {
        for (int j = 0; j < results.length; j++) {
            System.out.println(DateUtil.asLocalDateTime(new Timestamp(results[j])));
        }
        acAttendacneTemp.setAltSecondTimeFrom(DateUtil.asLocalDateTime(new Timestamp(results[i])).toLocalTime());
        acAttendacneTemp.setAltSecondTimeTo(DateUtil.asLocalDateTime(new Timestamp(results[i2])).toLocalTime());
        if (DateUtil.asLocalDateTime(new Timestamp(results[i2])).toLocalDate().isAfter(acAttendacneTemp.getDate())
        ) { // 计算出的时间是否晚于考勤日的日期
            acAttendacneTemp.setAltExtend2TimeFlag(true);
        } else {
            acAttendacneTemp.setAltExtend2TimeFlag(false);
        }
    }

    public void setThirdTime(Long[] results, AcAttendanceRecordTemp acAttendacneTemp, int i, int i2) {
        acAttendacneTemp.setAltThirdTimeFrom(DateUtil.asLocalDateTime(new Timestamp(results[i])).toLocalTime());
        acAttendacneTemp.setAltThirdTimeTo(DateUtil.asLocalDateTime(new Timestamp(results[i2])).toLocalTime());
        if (DateUtil.asLocalDateTime(new Timestamp(results[i2])).toLocalDate().isAfter(acAttendacneTemp.getDate())
        ) { // 计算出的时间是否晚于考勤日的日期
            acAttendacneTemp.setAltExtend3TimeFlag(true);
        } else {
            acAttendacneTemp.setAltExtend3TimeFlag(false);
        }
    }


    private void matchBeLate(LocalDate startDate, LocalDate endDate) {
        // 匹配厂车迟到
        AcBeLateDateQueryCriteria acBeLateDateQueryCriteria = new AcBeLateDateQueryCriteria();
        acBeLateDateQueryCriteria.setBeginDate(startDate);
        acBeLateDateQueryCriteria.setEndDate(endDate);
        acBeLateDateQueryCriteria.setEnabledFlag(true);
        acBeLateDateQueryCriteria.setLateFlag(true);
        List<AcBeLateDate> acBeLateDateList = acBeLateDateDao.listAllByCriteria(acBeLateDateQueryCriteria);
        for (AcBeLateDate beLate : acBeLateDateList
        ) {
            acAttendanceRecordTempDao.updateTimeFromByBeLate(beLate.getBeLateDate(), beLate.getId());
        }
        // 匹配提前下班
        acBeLateDateQueryCriteria.setLateFlag(false);
        acBeLateDateList = acBeLateDateDao.listAllByCriteria(acBeLateDateQueryCriteria);
        for (AcBeLateDate beLate : acBeLateDateList
        ) {
            acAttendanceRecordTempDao.updateTimeToByBeFore(beLate.getBeLateDate(), beLate.getId());
        }
    }

}
