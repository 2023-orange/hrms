package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.ac.dao.AcAttendanceRecordHistoryDao;
import com.sunten.hrms.ac.dao.AcVacateDao;
import com.sunten.hrms.ac.domain.AcAbnormalClockRecord;
import com.sunten.hrms.ac.domain.AcAttendanceRecordHistory;
import com.sunten.hrms.ac.domain.AcVacate;
import com.sunten.hrms.ac.dto.AcAttendanceRecordHistoryDTO;
import com.sunten.hrms.ac.dto.AcAttendanceRecordHistoryQueryCriteria;
import com.sunten.hrms.ac.service.AcAbnormalClockRecordService;
import com.sunten.hrms.utils.PageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AcAttendanceRecordHistoryServiceImplTest {
    @Autowired
    AcAttendanceRecordHistoryDao acAttendanceRecordHistoryDao;
    @Autowired
    AcVacateDao acVacateDao;
    @Autowired
    AcAbnormalClockRecordService acAbnormalClockRecordService;

    @Test
    public void testList() {        // 查出所有未pass且为等待的的考勤异常记录异常
        AcAttendanceRecordHistoryQueryCriteria acAttendanceRecordHistoryQueryCriteria = new AcAttendanceRecordHistoryQueryCriteria();
        acAttendanceRecordHistoryQueryCriteria.setDisposeFlag("wait");
        acAttendanceRecordHistoryQueryCriteria.setAbNormalFlag(true);
        // 有wait 的
        List<AcAttendanceRecordHistory> acAttendanceRecordHistories = acAttendanceRecordHistoryDao.listAllByCriteria(acAttendanceRecordHistoryQueryCriteria);

        for (AcAttendanceRecordHistory acAttendanceRecordHistory: acAttendanceRecordHistories
        ) {
            Boolean flag = true;
            for (AcAbnormalClockRecord acAbnormalClockRecord: acAttendanceRecordHistory.getClockRecords()
            ) {
                // 未复核完毕的异常打卡与OA的申请单匹配
                if (null != acAbnormalClockRecord && null != acAbnormalClockRecord.getCheckFlag() && !acAbnormalClockRecord.getCheckFlag() ) {
                    AcVacate acVacate = acVacateDao.getAcVacateByRequisitionCode(acAbnormalClockRecord.getReqCode());
                    if (null != acVacate && acVacate.getState().equals("Pass")) {
                        // 复核通过
                        acAbnormalClockRecord.setState(acVacate.getState());
                        acAbnormalClockRecord.setCheckFlag(true);
                        acAbnormalClockRecordService.update(acAbnormalClockRecord);
                    } else {
                        flag = false;
                        break;
                    }
                }
            }
            if (flag) { // 全部打卡异常审核通过，更新his
                acAttendanceRecordHistory.setDisposeFlag("pass");
                acAttendanceRecordHistoryDao.updateAllColumnByKey(acAttendanceRecordHistory);
            }
        }

    }

    @Test
    public void testQuery() {
        AcAttendanceRecordHistoryQueryCriteria acAttendanceRecordHistoryQueryCriteria = new AcAttendanceRecordHistoryQueryCriteria();
        acAttendanceRecordHistoryQueryCriteria.setAbNormalFlag(true);
        acAttendanceRecordHistoryQueryCriteria.setDisposeFlag("not");
        List<AcAttendanceRecordHistory> acAttendanceRecordHistories = acAttendanceRecordHistoryDao.listAllByCriteria(acAttendanceRecordHistoryQueryCriteria);
        for (AcAttendanceRecordHistory ac: acAttendanceRecordHistories
             ) {
            System.out.println(ac);
        }
    }

    @Test
    public void testListForBatch() {
        Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(650, 20, sort);
        Page<AcAttendanceRecordHistory> page = PageUtil.startPage(pageable);
        AcAttendanceRecordHistoryQueryCriteria acAttendanceRecordHistoryQueryCriteria = new AcAttendanceRecordHistoryQueryCriteria();
        acAttendanceRecordHistoryQueryCriteria.setBeginDate(LocalDate.of(2020,11,1));
        acAttendanceRecordHistoryQueryCriteria.setEndDate(LocalDate.of(2020,11,30));
        acAttendanceRecordHistoryQueryCriteria.setAbNormalFlag(true);
        acAttendanceRecordHistoryQueryCriteria.setDisposeFlag("notPass");
//        acAttendanceRecordHistoryQueryCriteria.setQueryName("00%");
        List<AcAttendanceRecordHistory> attendanceRecordHistorys = acAttendanceRecordHistoryDao.listForBatchByPage(page, acAttendanceRecordHistoryQueryCriteria);
        System.out.println(attendanceRecordHistorys);
    }

}
