package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.ac.dao.AcAttendanceRecordHistoryDao;
import com.sunten.hrms.ac.domain.AcAttendanceRecordHistory;
import com.sunten.hrms.ac.dto.AcAttendanceRecordHistoryDTO;
import com.sunten.hrms.ac.dto.AcAttendanceRecordHistoryQueryCriteria;
import com.sunten.hrms.ac.service.AcAttendanceRecordHistoryService;
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
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AcAttendanceRecordHistoryServiceImplTest1 {
    @Autowired
    AcAttendanceRecordHistoryService acAttendanceRecordHistoryService;

    @Autowired
    AcAttendanceRecordHistoryDao acAttendanceRecordHistoryDao;

    @Test
    public void listAll() {
        Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
//        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(650, 20, sort);
        AcAttendanceRecordHistoryQueryCriteria criteria = new AcAttendanceRecordHistoryQueryCriteria();
        criteria.setAbNormalFlag(true);
        criteria.setDisposeFlag("not");
//        criteria.setQueryName("%");
//        acAttendanceRecordHistoryService.listAll(criteria,pageable);
        Page<AcAttendanceRecordHistory> page = PageUtil.startPage(pageable);
        page.setSearchCount(false);
        List<AcAttendanceRecordHistory> attendanceRecordHistorys = acAttendanceRecordHistoryDao.listAllByCriteriaPage(page, criteria);

    }


    @Test
    public void listAll1() {
        Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
//        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(1600, 20, sort);
        AcAttendanceRecordHistoryQueryCriteria criteria = new AcAttendanceRecordHistoryQueryCriteria();
//        criteria.setAbNormalFlag(true);
//        criteria.setDisposeFlag("not");
//        criteria.setQueryName("%");
        criteria.setBeginDate(LocalDate.of(2020,10,1));
        criteria.setEndDate(LocalDate.of(2020,11,30));
        criteria.setDisposeFlag("not");
//        acAttendanceRecordHistoryService.listForBatchByPage(pageable, criteria);
        Page<AcAttendanceRecordHistory> page = PageUtil.startPage(pageable);
        page.setSearchCount(false);
        System.out.println(page.offset());
        acAttendanceRecordHistoryDao.listForBatchByPage(page, criteria);
        System.out.println(page.offset());
    }
}
