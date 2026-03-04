package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.dao.AcClockRecordDao;
import com.sunten.hrms.ac.domain.AcClockRecord;
import com.sunten.hrms.ac.dto.AcClockRecordQueryCriteria;
import com.sunten.hrms.ac.service.AcClockRecordService;
import com.sunten.hrms.config.FndDataScope;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @atuthor xukai
 * @date 2020/10/20 10:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AcClockRecordServiceImplTest {

    @Autowired
    AcClockRecordService acClockRecordService;

    @Autowired
    AcClockRecordDao acClockRecordDao;

    @Test
    public void testListAll(){
        AcClockRecordQueryCriteria criteria = new AcClockRecordQueryCriteria();
        Set<Long> depts = new HashSet<>();
        depts.add(67L);depts.add(69L);depts.add(11L);depts.add(62L);depts.add(63L);depts.add(64L);depts.add(65L);
        depts.add(68L);depts.add(98L);depts.add(58L);depts.add(48L);depts.add(38L);depts.add(28L);depts.add(18L);
        criteria.setDeptIds(depts);
        Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(0, 10, sort);

        Map<String,Object> map = acClockRecordService.listAll(criteria,pageable);

        System.out.println("执行完毕");
    }

    @Test
    public void testData0() { // fei 0 jie duan data
        AcClockRecord acClockRecord1 = new AcClockRecord();
        // rest_day_clock
//        acClockRecord1.setEmployeeId(183L).setCardMachineNo("test").setDate(LocalDate.of(2020, 10,11)).setClockTime(LocalDateTime.now().toLocalTime());
//        acClockRecordDao.insertAllColumn(acClockRecord1);

//        AcClockRecord acClockRecord2 = new AcClockRecord();
//        acClockRecord2.setEmployeeId(183L).setCardMachineNo("test").setDate(LocalDate.of(2020,10,12)).setClockTime(LocalTime.of(13,50,0));
//        acClockRecordDao.insertAllColumn(acClockRecord2);
//        AcClockRecord acClockRecord22 = new AcClockRecord();
//        acClockRecord2.setEmployeeId(183L).setCardMachineNo("test").setDate(LocalDate.of(2020,10,12)).setClockTime(LocalTime.of(13,55,0));
//        acClockRecordDao.insertAllColumn(acClockRecord22);
        // shang yi ri fei rest
//        AcClockRecord acClockRecord3 = new AcClockRecord();
//        acClockRecord3.setEmployeeId(183L).setCardMachineNo("test").setDate(LocalDate.of(2020,10,12)).setClockTime(LocalTime.of(13,55,0));

//        AcClockRecord acClockRecord11 = new AcClockRecord();
//        acClockRecord11.setEmployeeId(183L).setCardMachineNo("test").setDate(LocalDate.of(2020,10,12)).setClockTime(LocalTime.of(22,50,0));
//        acClockRecordDao.insertAllColumn(acClockRecord11);
        AcClockRecord acClockRecord11 = new AcClockRecord();
        acClockRecord11.setEmployeeId(183L).setCardMachineNo("test").setDate(LocalDate.of(2020,10,22)).setClockTime(LocalTime.of(0,30,0));
        acClockRecordDao.insertAllColumn(acClockRecord11);
    }

    @Test
    public void testNo0Data() {
//        AcClockRecord acClockRecord1 = new AcClockRecord();
//        acClockRecord1.setEmployeeId(183L).setCardMachineNo("test").setDate(LocalDate.of(2020, 10,12)).setClockTime(LocalTime.of(12,0,0));
//        acClockRecordDao.insertAllColumn(acClockRecord1);
//        AcClockRecord acClockRecord2 = new AcClockRecord();
//        acClockRecord2.setEmployeeId(183L).setCardMachineNo("test").setDate(LocalDate.of(2020, 10,11)).setClockTime(LocalTime.of(23,50,0));
//        acClockRecordDao.insertAllColumn(acClockRecord2);
//        AcClockRecord acClockRecord3 = new AcClockRecord();
//        acClockRecord3.setEmployeeId(183L).setCardMachineNo("test").setDate(LocalDate.of(2020, 10,15)).setClockTime(LocalTime.of(2,0,0));
//        acClockRecordDao.insertAllColumn(acClockRecord3);

        AcClockRecord acClockRecord2 = new AcClockRecord();
        acClockRecord2.setEmployeeId(183L).setCardMachineNo("test").setDate(LocalDate.of(2020, 10,20)).setClockTime(LocalTime.of(18,0,0));
        acClockRecordDao.insertAllColumn(acClockRecord2);
        AcClockRecord acClockRecord3 = new AcClockRecord();
        acClockRecord3.setEmployeeId(183L).setCardMachineNo("test").setDate(LocalDate.of(2020, 10,21)).setClockTime(LocalTime.of(18,0,0));
        acClockRecordDao.insertAllColumn(acClockRecord3);
        AcClockRecord acClockRecord4 = new AcClockRecord();
        acClockRecord4.setEmployeeId(183L).setCardMachineNo("test").setDate(LocalDate.of(2020, 10,21)).setClockTime(LocalTime.of(18,50,0));
        acClockRecordDao.insertAllColumn(acClockRecord4);
    }
}
