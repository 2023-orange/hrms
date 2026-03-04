package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.dao.AcLeaveFormDao;
import com.sunten.hrms.ac.dao.AcLeaveFormTempDao;
import com.sunten.hrms.ac.domain.AcAttendanceRecordTemp;
import com.sunten.hrms.ac.domain.AcLeaveForm;
import com.sunten.hrms.ac.domain.AcLeaveFormTemp;
import com.sunten.hrms.ac.domain.AcRegime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AcLeaveFormTempServiceImplTest {
    @Autowired
    AcLeaveFormDao acLeaveFormDao;

    @Autowired
    AcLeaveFormTempDao acLeaveFormTempDao;

    @Test
    public void testGetLeaveForm () {
        List<AcLeaveForm> acLeaveForms = acLeaveFormDao.getLeaveFormByDate(LocalDate.now().minusDays(150),LocalDate.now());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String[] ss = "yyyy-MM-dd HH:mm:ss".split(":");
        System.out.println(ss.length);
        List<AcLeaveFormTemp> insertLeaveForms = new ArrayList<>();
        for (AcLeaveForm acForm : acLeaveForms
             ) {
            AcLeaveFormTemp acLeaveFormTemp = new AcLeaveFormTemp();
            acLeaveFormTemp.setWorkCard(acForm.getWorkCard());
            if (acForm.getStartTime().split(":").length == 3) {
                acLeaveFormTemp.setStartTime(LocalDateTime.parse(acForm.getStartTime(),df));
            } else {
                acLeaveFormTemp.setStartTime(LocalDateTime.parse(acForm.getStartTime(),df1));
            }
            if (acForm.getEndTime().split(":").length == 3) {
                acLeaveFormTemp.setEndTime(LocalDateTime.parse(acForm.getEndTime(),df));
            } else {
                acLeaveFormTemp.setEndTime(LocalDateTime.parse(acForm.getEndTime(),df1));
            }
//            acLeaveFormTempDao.insertAllColumn(acLeaveFormTemp);
            insertLeaveForms.add(acLeaveFormTemp);
        }
        // 限制2100  ，每组7条， 每250发一次
        System.out.println(insertLeaveForms);

        Integer count = insertLeaveForms.size()/250;
        Integer dCount = insertLeaveForms.size()%250;
        for (int i = 0; i <= count ; i++) {
            List<AcLeaveFormTemp> acLeaveFormTemps = new ArrayList<>();
            if (i == count) {
                if (dCount == 0) {
                    for (int j = i * 250; j < (i+1) * 250 ; j++) {
                        System.out.println(j);
                        acLeaveFormTemps.add(insertLeaveForms.get(j));
                    }
                    acLeaveFormTempDao.insertCollection(acLeaveFormTemps);
                } else {
                    for (int j = i * 250; j < i * 250 + dCount; j++) {
                        System.out.println(j);
                        acLeaveFormTemps.add(insertLeaveForms.get(j));
                    }
                    acLeaveFormTempDao.insertCollection(acLeaveFormTemps);
                }
            } else {
                for (int j = i * 250; j < (i+1) * 250 ; j++) {
                    System.out.println(j);
                    acLeaveFormTemps.add(insertLeaveForms.get(j));
                }
                acLeaveFormTempDao.insertCollection(acLeaveFormTemps);
            }
        }

        List<AcLeaveFormTemp> acLeaveFormTemps = acLeaveFormTempDao.listAllByMatchEmp();
        for (AcLeaveFormTemp acLeaveFormTemp: acLeaveFormTemps
             ) {
            System.out.println(acLeaveFormTemp);
        }
//        acLeaveFormTempDao.deleteAll();
    }

    @Test
    public void insertTest () {


        AcLeaveFormTemp acLeaveFormTemp2 = new AcLeaveFormTemp();
        acLeaveFormTemp2.setWorkCard("5005");
        acLeaveFormTemp2.setStartTime(LocalDateTime.of(2020,10,19,14,0,0));
        acLeaveFormTemp2.setEndTime(LocalDateTime.of(2020,10,19,16,0,0));
        acLeaveFormTempDao.insertAllColumn(acLeaveFormTemp2);
        acLeaveFormTemp2.setStartTime(LocalDateTime.of(2020,10,15,12,0,0));
        acLeaveFormTemp2.setEndTime(LocalDateTime.of(2020,10,16,14,0,0));
        acLeaveFormTempDao.insertAllColumn(acLeaveFormTemp2);
        acLeaveFormTemp2.setStartTime(LocalDateTime.of(2020,10,12,14,0,0));
        acLeaveFormTemp2.setEndTime(LocalDateTime.of(2020,10,12,16,0,0));
        acLeaveFormTempDao.insertAllColumn(acLeaveFormTemp2);
    }
    @Test
    public void test (){
        AcRegime acRegime = new AcRegime();
        System.out.println(acRegime.getAttribute1());
    }

    public Long[] getFromTo(LocalDateTime fromTime, LocalDateTime toTime,LocalDateTime startTime,LocalDateTime endTime){
        Long fromTimeL = fromTime.toEpochSecond(ZoneOffset.of("+8"));
        Long toTimeL = toTime.toEpochSecond(ZoneOffset.of("+8"));
        Long startTimeL = startTime.toEpochSecond(ZoneOffset.of("+8"));
        Long endTimeL = endTime.toEpochSecond(ZoneOffset.of("+8"));
        Long[] longs = new Long[4];
        if ((fromTimeL >= startTimeL && fromTimeL >= endTimeL && toTimeL >= startTimeL && toTimeL >= endTimeL) ||
                (fromTimeL <= startTimeL && fromTimeL <= endTimeL && toTimeL <= startTimeL && toTimeL <= endTimeL)
        ) {
            longs[0] = fromTimeL;
            longs[1] = toTimeL;
        }
        if (fromTimeL <= endTimeL && fromTimeL >= startTimeL && toTimeL <= endTimeL && toTimeL >= startTimeL) {
            return null;
        }
        if (fromTimeL < startTimeL && fromTimeL < endTimeL && toTimeL > startTimeL && toTimeL < endTimeL  ) { // 请假的开始时间在段内
            longs[0] = fromTimeL;
            longs[1] = startTimeL;
        }
        if (fromTimeL > startTimeL && fromTimeL < endTimeL && toTimeL > endTimeL) { // 请假的结束时间在段内
            longs[0] = endTimeL;
            longs[1] = fromTimeL;
        }
        if (fromTimeL > startTimeL && fromTimeL < endTimeL && toTimeL < endTimeL) { // 请假的开始时间与结束时间在段内
            longs[0] = fromTimeL;
            longs[1] = startTimeL;
            longs[2] = endTimeL;
            longs[3] = toTimeL;

        }
        return longs;
    }

//    public List<AcAttendanceRecordTemp> matchLeaveForm(List<AcLeaveFormTemp> acLeaveFormTemps, List<AcAttendanceRecordTemp> acAttendanceRecordTemps) {
//        for (AcLeaveFormTemp acLeaveFormTemp: acLeaveFormTemps
//             ) {
//            for (AcAttendanceRecordTemp acAttendanceRecordTemp : acAttendanceRecordTemps
//                 ) {
//                if () {
//
//                }
//            }
//        }
//    }
}
