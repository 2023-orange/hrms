package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.*;
import com.sunten.hrms.ac.domain.*;
import com.sunten.hrms.ac.dto.AcAbnormalAttendanceRecordDTO;
import com.sunten.hrms.ac.dto.AcAttendanceRecordTempDTO;
import com.sunten.hrms.ac.dto.AcAttendanceRecordTempQueryCriteria;
import com.sunten.hrms.ac.dto.AcBeLateDateQueryCriteria;
import com.sunten.hrms.ac.mapper.AcAttendanceRecordTempMapper;
import com.sunten.hrms.ac.service.AcAbnormalAttendanceRecordService;
import com.sunten.hrms.ac.service.AcAttendanceRecordTempService;
import com.sunten.hrms.ac.service.AcLeaveFormTempService;
import com.sunten.hrms.ac.util.AcUtil;
import com.sunten.hrms.ac.vo.AcRegimeTimeVo;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.utils.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 考勤处理记录临时表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcAttendanceRecordTempServiceImpl extends ServiceImpl<AcAttendanceRecordTempDao, AcAttendanceRecordTemp> implements AcAttendanceRecordTempService {
    private final AcAttendanceRecordTempDao acAttendanceRecordTempDao;
    private final AcAttendanceRecordTempMapper acAttendanceRecordTempMapper;
    private final AcAbnormalAttendanceRecordDao acAbnormalAttendanceRecordDao;
    private final AcLeaveFormTempDao acLeaveFormTempDao;
    private final AcLeaveFormDao acLeaveFormDao;
    private final AcSetUpDao acSetUpDao;
    private final AcAbnormalAttendanceRecordService acAbnormalAttendanceRecordService;
    private final AcBeLateDateDao acBeLateDateDao;
    @Autowired
    private AcLeaveApplicationDao acLeaveApplicationDao;
    @Autowired
    private AcAttendanceRecordTempServiceImpl instance;

    public AcAttendanceRecordTempServiceImpl(AcAttendanceRecordTempDao acAttendanceRecordTempDao, AcAttendanceRecordTempMapper acAttendanceRecordTempMapper,
                                             AcAbnormalAttendanceRecordDao acAbnormalAttendanceRecordDao,
                                             AcLeaveFormDao acLeaveFormDao, AcLeaveFormTempDao acLeaveFormTempDao,
                                             AcSetUpDao acSetUpDao,AcBeLateDateDao acBeLateDateDao,
                                             AcAbnormalAttendanceRecordService acAbnormalAttendanceRecordService) {
        this.acAttendanceRecordTempDao = acAttendanceRecordTempDao;
        this.acAttendanceRecordTempMapper = acAttendanceRecordTempMapper;
        this.acAbnormalAttendanceRecordDao = acAbnormalAttendanceRecordDao;
        this.acLeaveFormTempDao = acLeaveFormTempDao;
        this.acLeaveFormDao = acLeaveFormDao;
        this.acSetUpDao = acSetUpDao;
        this.acAbnormalAttendanceRecordService = acAbnormalAttendanceRecordService;
        this.acBeLateDateDao = acBeLateDateDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcAttendanceRecordTempDTO insert(AcAttendanceRecordTemp attendanceRecordTempNew) {
        acAttendanceRecordTempDao.insertAllColumn(attendanceRecordTempNew);
        return acAttendanceRecordTempMapper.toDto(attendanceRecordTempNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcAttendanceRecordTemp attendanceRecordTemp = new AcAttendanceRecordTemp();
        attendanceRecordTemp.setId(id);
        this.delete(attendanceRecordTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcAttendanceRecordTemp attendanceRecordTemp) {
        // TODO    确认删除前是否需要做检查
        acAttendanceRecordTempDao.deleteByEntityKey(attendanceRecordTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcAttendanceRecordTemp attendanceRecordTempNew) {
        AcAttendanceRecordTemp attendanceRecordTempInDb = Optional.ofNullable(acAttendanceRecordTempDao.getByKey(attendanceRecordTempNew.getId())).orElseGet(AcAttendanceRecordTemp::new);
        ValidationUtil.isNull(attendanceRecordTempInDb.getId(), "AttendanceRecordTemp", "id", attendanceRecordTempNew.getId());
        attendanceRecordTempNew.setId(attendanceRecordTempInDb.getId());
        acAttendanceRecordTempDao.updateAllColumnByKey(attendanceRecordTempNew);
    }

    @Override
    public AcAttendanceRecordTempDTO getByKey(Long id) {
        AcAttendanceRecordTemp attendanceRecordTemp = Optional.ofNullable(acAttendanceRecordTempDao.getByKey(id)).orElseGet(AcAttendanceRecordTemp::new);
        ValidationUtil.isNull(attendanceRecordTemp.getId(), "AttendanceRecordTemp", "id", id);
        return acAttendanceRecordTempMapper.toDto(attendanceRecordTemp);
    }

    @Override
    public List<AcAttendanceRecordTempDTO> listAll(AcAttendanceRecordTempQueryCriteria criteria) {
        return acAttendanceRecordTempMapper.toDto(acAttendanceRecordTempDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcAttendanceRecordTempQueryCriteria criteria, Pageable pageable) {
        Page<AcAttendanceRecordTemp> page = PageUtil.startPage(pageable);
        List<AcAttendanceRecordTemp> attendanceRecordTemps = acAttendanceRecordTempDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acAttendanceRecordTempMapper.toDto(attendanceRecordTemps), page.getTotal());
    }

    @Override
    public void download(List<AcAttendanceRecordTempDTO> attendanceRecordTempDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcAttendanceRecordTempDTO attendanceRecordTempDTO : attendanceRecordTempDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("异常考勤执行记录id", attendanceRecordTempDTO.getAbnromalAttendanceRecordId());
            map.put("员工id", attendanceRecordTempDTO.getEmployeeId());
            map.put("日期", attendanceRecordTempDTO.getDate());
            map.put("上一段的结束时间（空意味上一天是休息日，非空意味上一天非休息日，时间为上一天的最后一段的结束时间）", attendanceRecordTempDTO.getLastParagraphEndTime());
            map.put("一班时间开始", attendanceRecordTempDTO.getFirstTimeFrom());
            map.put("一班时间结束", attendanceRecordTempDTO.getFirstTimeTo());
            map.put("一班是否跨日", attendanceRecordTempDTO.getExtend1TimeFlag());
            map.put("二班时间开始", attendanceRecordTempDTO.getSecondTimeFrom());
            map.put("二班时间结束", attendanceRecordTempDTO.getSecondTimeTo());
            map.put("二班是否跨日", attendanceRecordTempDTO.getExtend2TimeFlag());
            map.put("三班时间开始", attendanceRecordTempDTO.getThirdTimeFrom());
            map.put("三班时间结束", attendanceRecordTempDTO.getThirdTimeTo());
            map.put("三班是否跨日", attendanceRecordTempDTO.getExtend3TimeFlag());
            map.put("下一段的开始时间（空意味着下一天没排班或者位休息日，非空下一天为工作日，时间为下一天的第一段的开始时间）", attendanceRecordTempDTO.getNextPeriodStartTime());
            map.put("是否休息", attendanceRecordTempDTO.getRestFlag());
            map.put("是否无排班（异常标记）", attendanceRecordTempDTO.getNoScheduling());
            map.put("是否休息日打卡（异常标记）", attendanceRecordTempDTO.getRestDayClock());
            map.put("是否全天未打卡（异常标记）", attendanceRecordTempDTO.getAllDayNoClock());
            map.put("是否上班时间打卡异常（异常标记）", attendanceRecordTempDTO.getOfficeTimeClock());
            map.put("是否上班未打卡（异常标记）", attendanceRecordTempDTO.getNoClockIn());
            map.put("是否上下班打卡次数异常（异常标记）", attendanceRecordTempDTO.getAbnormalClockInTime());
            map.put("是否下班未打卡异常（异常标记）", attendanceRecordTempDTO.getNoClockAfterWork());
            map.put("一班时间开始修正", attendanceRecordTempDTO.getAltFirstTimeFrom());
            map.put("一班时间结束修正", attendanceRecordTempDTO.getAltFirstTimeTo());
            map.put("一班是否跨日修正", attendanceRecordTempDTO.getAltExtend1TimeFlag());
            map.put("二班时间开始修正", attendanceRecordTempDTO.getAltSecondTimeFrom());
            map.put("二班时间结束修正", attendanceRecordTempDTO.getAltSecondTimeTo());
            map.put("二班是否跨日修正", attendanceRecordTempDTO.getAltExtend2TimeFlag());
            map.put("三班时间开始修正", attendanceRecordTempDTO.getAltThirdTimeFrom());
            map.put("三班时间结束修正", attendanceRecordTempDTO.getAltThirdTimeTo());
            map.put("三班是否跨日修正", attendanceRecordTempDTO.getAltExtend3TimeFlag());
            map.put("是否休息修正", attendanceRecordTempDTO.getAltRestDayFlag());
            map.put("弹性域1", attendanceRecordTempDTO.getAttribute1());
            map.put("弹性域2", attendanceRecordTempDTO.getAttribute2());
            map.put("弹性域3", attendanceRecordTempDTO.getAttribute3());
            map.put("弹性域4", attendanceRecordTempDTO.getAttribute4());
            map.put("弹性域5", attendanceRecordTempDTO.getAttribute5());
            map.put("id", attendanceRecordTempDTO.getId());
            map.put("updateTime", attendanceRecordTempDTO.getUpdateTime());
            map.put("updateBy", attendanceRecordTempDTO.getUpdateBy());
            map.put("createTime", attendanceRecordTempDTO.getCreateTime());
            map.put("createBy", attendanceRecordTempDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateAbnormalAttendance() {
        // 获取定时任务里的是否截断0点
        AcSetUp acSetUp = acSetUpDao.getByNew();
        // 先获取上一次执行成功的出异常的结束日期
        AcAbnormalAttendanceRecord acAbnormalAttendanceRecord = acAbnormalAttendanceRecordDao.getLastOne();
        // 结束日期为今天减2日
        LocalDate endDate = LocalDate.now().minusDays(2);
//        LocalDate endDate = LocalDate.of(2021,6,30);
        // 开始日期
        LocalDate startDate;
        if (null != acAbnormalAttendanceRecord && null != acAbnormalAttendanceRecord.getEndTime()) { // 有上一次执行
            // 日期+1
            startDate = acAbnormalAttendanceRecord.getEndTime().plusDays(1);
        } else {
            startDate = endDate.minusDays(endDate.getDayOfMonth() - 1); // LocalDate.of(2020,6,1);
        }
        AcAbnormalAttendanceRecord addRecord = new AcAbnormalAttendanceRecord();
        addRecord.setStartTime(startDate);
        addRecord.setEndTime(endDate);
        addRecord.setExecutionTime(LocalDateTime.now());
        if (startDate.isAfter(endDate)) {
            // 起始时间晚于结束时间，不跑
            addRecord.setExecutionStatus("ERROR");
            addRecord.setAttribute1("起始时间晚于结束时间");
            acAbnormalAttendanceRecordService.insert(addRecord);
        } else {
            addRecord.setExecutionStatus("RUNNING");
            AcAbnormalAttendanceRecordDTO acAbnormalAttendanceRecordDTO = acAbnormalAttendanceRecordService.insert(addRecord);
            try {
                instance.acAttendanceRecordTempFunction(startDate, endDate, acAbnormalAttendanceRecordDTO, acSetUp);
                addRecord.setExecutionStatus("SUCCESS");
            } catch (Exception e) {
                // 更新
                addRecord.setAttribute1(ThrowableUtil.getStackTrace(e));
                addRecord.setExecutionStatus("ERROR");
            } finally {
                acAbnormalAttendanceRecordService.update(addRecord);
            }
        }
    }

    List<AcLeaveFormTemp> getLeaveFromTempByDate(LocalDate startTime, LocalDate endTime) {
        // 先清除临时表里的旧数据
        acLeaveFormTempDao.deleteAll();
        // 获取请假条 2024-03-02 对这里做修改 这里原来是从OA获取已经通过审批的请假单的
//        List<AcLeaveForm> acLeaveForms = acLeaveFormDao.getLeaveFormByDate(startTime, endTime);
        // 现在从HRMS当中去取通过审批的请假单
        List<AcLeaveForm> acLeaveForms = acLeaveApplicationDao.getLeaveFormByDate(startTime, endTime);
//        System.out.println("acLeaveForms: " + acLeaveForms);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<AcLeaveFormTemp> insertLeaveForms = new ArrayList<>();
        for (AcLeaveForm acForm : acLeaveForms
        ) {
            AcLeaveFormTemp acLeaveFormTemp = new AcLeaveFormTemp();
            acLeaveFormTemp.setWorkCard(acForm.getWorkCard());
            if (acForm.getStartTime().split(":").length == 3) {
                acLeaveFormTemp.setStartTime(LocalDateTime.parse(acForm.getStartTime(), df));
            } else {
                acLeaveFormTemp.setStartTime(LocalDateTime.parse(acForm.getStartTime(), df1));
            }
            if (acForm.getEndTime().split(":").length == 3) {
                acLeaveFormTemp.setEndTime(LocalDateTime.parse(acForm.getEndTime(), df));
            } else {
                acLeaveFormTemp.setEndTime(LocalDateTime.parse(acForm.getEndTime(), df1));
            }
            insertLeaveForms.add(acLeaveFormTemp);
        }
        // 限制2100 个参数每次preStatement ，每组7个参数， 每250发一次
        if (!insertLeaveForms.isEmpty()) {
            System.out.println(acLeaveForms.size());
            List<AcLeaveFormTemp> acLeaveFormTemps = new ArrayList<>();
            int maxForEach = 250;
            int insertCount = 1;
            for (; insertCount <= insertLeaveForms.size(); insertCount++) {
                acLeaveFormTemps.add(insertLeaveForms.get(insertCount - 1));
                if (insertCount % maxForEach == 0) {
                    acLeaveFormTempDao.insertCollection(acLeaveFormTemps);
                    acLeaveFormTemps = new ArrayList<>();
                }
            }
            if (insertCount % maxForEach != 0) {
                acLeaveFormTempDao.insertCollection(acLeaveFormTemps);
            }
        }
        return acLeaveFormTempDao.listAllByMatchEmp();
    }

//    @Transactional(rollbackFor = Exception.class)
//    public void testTransaction() {
//        // insert与update分开事务
//        AcAbnormalAttendanceRecord acAbnormalAttendanceRecord = new AcAbnormalAttendanceRecord();
//        AcAbnormalAttendanceRecordDTO acAbnormalAttendanceRecordDTO = null;
//        acAbnormalAttendanceRecord.setExecutionStatus("595");
//        acAbnormalAttendanceRecord.setStartTime(LocalDate.now());
//        acAbnormalAttendanceRecord.setEndTime(LocalDate.now());
//        acAbnormalAttendanceRecord.setExecutionTime(LocalDateTime.now());
//        acAbnormalAttendanceRecordDTO = acAbnormalAttendanceRecordService.insert(acAbnormalAttendanceRecord);
//
////        Object savePoint = null;
//        try {
////            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//
////            savePoint =  TransactionAspectSupport.currentTransactionStatus().createSavepoint();
//            instance.TrainTest();
//        } catch (Exception e) {
//            e.printStackTrace();
//            acAbnormalAttendanceRecord.setExecutionStatus("error23");
//            acAbnormalAttendanceRecord.setId(acAbnormalAttendanceRecordDTO.getId());
//            acAbnormalAttendanceRecordService.update(acAbnormalAttendanceRecord);
//
////                if(null != savePoint) {
////            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
//        }
//    }
//    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
//    public void TrainTest() {
//        AcLeaveFormTemp acLeaveForm = new AcLeaveFormTemp();
//        acLeaveForm.setWorkCard("aaaaaaaaaaa");
//        acLeaveForm.setStartTime(LocalDateTime.now());
//        acLeaveForm.setEndTime(LocalDateTime.now());
//        acLeaveFormTempService.insert(acLeaveForm);
//
//        AcLeaveFormTemp acLeaveFormTemp = new AcLeaveFormTemp();
//        acLeaveFormTemp.setStartTime(LocalDateTime.now());
//        acLeaveFormTemp.setEndTime(LocalDateTime.now());
//        acLeaveFormTempService.insert(acLeaveFormTemp);
//    }


    void matchLeaveFrom(List<AcLeaveFormTemp> acLeaveFormTemps) {
        AcAttendanceRecordTempQueryCriteria acAttendanceRecordTempQueryCriteria;
        for (AcLeaveFormTemp acLeaveFormTemp : acLeaveFormTemps) {
            // 查询该员工在该段请假时间内的且不为休息日的排班
            acAttendanceRecordTempQueryCriteria = new AcAttendanceRecordTempQueryCriteria();
            acAttendanceRecordTempQueryCriteria.setEmployeeId(acLeaveFormTemp.getPmEmployee().getId());
            acAttendanceRecordTempQueryCriteria.setStartDate(acLeaveFormTemp.getStartTime().toLocalDate());
            acAttendanceRecordTempQueryCriteria.setEndDate(acLeaveFormTemp.getEndTime().toLocalDate());
            acAttendanceRecordTempQueryCriteria.setNoSchedulingFlag(false);
            acAttendanceRecordTempQueryCriteria.setRestFlag(false);
//            log.debug((++count)+"===========================listAllByCriteria=============================");
//            log.debug(acAttendanceRecordTempQueryCriteria.toString());
            List<AcAttendanceRecordTemp> acAttendanceRecordTemps = acAttendanceRecordTempDao.listAllByCriteria(acAttendanceRecordTempQueryCriteria);
            for (AcAttendanceRecordTemp acAttendacneTemp : acAttendanceRecordTemps) {
                if (acAttendacneTemp.getDate().equals(acLeaveFormTemp.getStartTime().toLocalDate()) || acAttendacneTemp.getDate().equals(acLeaveFormTemp.getEndTime().toLocalDate())) {
                    // 工作时间段与请假时间段有交集, 将三段放入集合循环
//                    log.debug("========================================================");
//                    log.debug(acAttendacneTemp.toString());
                    List<AcRegimeTimeVo> acRegimeTimeVos = AcUtil.transferAttendanceAltTimeToTimeList(acAttendacneTemp, false);
                    List<AcRegimeTimeVo> timeVosMatchResult = new ArrayList<>();
                    for (AcRegimeTimeVo vo : acRegimeTimeVos) {
//                        log.debug(vo.toString());
                        LocalDateTime timeFrom = acAttendacneTemp.getDate().atTime(vo.getTimeFrom());
                        LocalDateTime timeTo = acAttendacneTemp.getDate().atTime(vo.getTimeTo());
                        // 跨日 + 1 日
                        if (vo.getExtendTimeFlag()) {
                            timeTo = timeTo.plusDays(1);
                        }
                        LocalDateTime leaveFrom = acLeaveFormTemp.getStartTime();
                        LocalDateTime leaveTo = acLeaveFormTemp.getEndTime();
                        // 处理完后的时间段
                        List<LocalDateTime> times = AcUtil.altTimeByLeave(timeFrom, timeTo, leaveFrom, leaveTo);
                        if (times != null) {
                            if (times.size() == 2 || times.size() == 4) {
                                // 有两段时取 ，0 1
                                timeVosMatchResult.add(AcUtil.createAcRegimeTimeVo(acAttendacneTemp, times, 0, 1));
                                if (times.size() == 4) {
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
                } else {
                    AcUtil.setAttendanceRestDay(acAttendacneTemp);
                }
                acAttendanceRecordTempDao.updateAllColumnByKey(acAttendacneTemp);
            }
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

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void acAttendanceRecordTempFunction(LocalDate startDate, LocalDate endDate, AcAbnormalAttendanceRecordDTO acAbnormalAttendanceRecordDTO, AcSetUp acSetUp) {
        AcAttendanceRecordTemp insertTemp = new AcAttendanceRecordTemp();
        insertTemp.setStartDate(startDate);
        insertTemp.setEndDate(endDate);
        insertTemp.setRecordId(acAbnormalAttendanceRecordDTO.getId());
        insertTemp.setCreateTime(LocalDateTime.now());
        insertTemp.setUpdateTime(LocalDateTime.now());
        insertTemp.setUpdateBy(-1L);
        insertTemp.setCreateBy(-1L);
        acAttendanceRecordTempDao.insertTempByEmployee(insertTemp);
        acAttendanceRecordTempDao.insertTempByDept(insertTemp);
        // 获取请假条
        List<AcLeaveFormTemp> acLeaveFormTemps = this.getLeaveFromTempByDate(startDate, endDate);
        // 对请假条进行匹配
        this.matchLeaveFrom(acLeaveFormTemps);
        // 获取厂车迟到, 进行匹配
        this.matchBeLate(startDate, endDate);
        if (!acSetUp.getStageFlag()) { // 不以 0 点为截断
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


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLeaveEmployeeServiceImpl() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        LocalDate startDate = calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        LocalDate endDate = calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        acAttendanceRecordTempDao.updateLeaveEmployee(startDate, endDate);
    }

}
