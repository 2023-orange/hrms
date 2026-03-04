package com.sunten.hrms.ac.dao;
import com.sunten.hrms.ac.domain.AcAttendanceRecordTemp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class AcAttendanceRecordTempDaoTest {
    @Autowired
    AcAttendanceRecordTempDao acAttendanceRecordTempDao;
    @Test
    public void insertTempByDept() { // 生成dept的排班
        AcAttendanceRecordTemp acAttendanceRecordTemp = new AcAttendanceRecordTemp();
        acAttendanceRecordTemp.setUpdateBy(-1L);
        acAttendanceRecordTemp.setCreateBy(-1L);
        acAttendanceRecordTemp.setUpdateTime(LocalDateTime.now());
        acAttendanceRecordTemp.setCreateTime(LocalDateTime.now());
        LocalDate date1 = LocalDate.of(2020,10,22);
        LocalDate date2 = LocalDate.of(2020,10,22);
//        acAttendanceRecordTemp.setInsertDate(date);
        acAttendanceRecordTemp.setRecordId(1L);
        acAttendanceRecordTemp.setStartDate(date1);
        acAttendanceRecordTemp.setEndDate(date2);
        acAttendanceRecordTempDao.insertTempByDept(acAttendanceRecordTemp);
    }

    @Test
    public void insertTempByEmployee() { // 生成emp的排班
        AcAttendanceRecordTemp acAttendanceRecordTemp = new AcAttendanceRecordTemp();
        LocalDate date1 = LocalDate.of(2020,10,22);
        LocalDate date2 = LocalDate.of(2020,10,22);
        acAttendanceRecordTemp.setUpdateBy(-1L);
        acAttendanceRecordTemp.setCreateBy(-1L);
        acAttendanceRecordTemp.setUpdateTime(LocalDateTime.now());
        acAttendanceRecordTemp.setCreateTime(LocalDateTime.now());
        acAttendanceRecordTemp.setStartDate(date1);
        acAttendanceRecordTemp.setEndDate(date2);
        acAttendanceRecordTemp.setRecordId(1L);
        acAttendanceRecordTempDao.insertTempByEmployee(acAttendanceRecordTemp);
    }

    @Test
    public void testAcAttendanceRecordTemp() {

    }
}
