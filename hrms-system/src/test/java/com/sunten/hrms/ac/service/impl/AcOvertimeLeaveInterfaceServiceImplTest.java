package com.sunten.hrms.ac.service.impl;


import com.sunten.hrms.ac.dao.AcOvertimeLeaveDao;
import com.sunten.hrms.ac.dao.AcOvertimeLeaveInterfaceDao;
import com.sunten.hrms.ac.domain.AcOvertimeLeave;
import com.sunten.hrms.ac.domain.AcOvertimeLeaveInterface;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveInterfaceQueryCriteria;
import com.sunten.hrms.ac.service.AcOvertimeLeaveInterfaceService;
import com.sunten.hrms.ac.service.AcOvertimeLeaveService;
import com.sunten.hrms.fnd.dao.FndInterfaceOperationRecordDao;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dto.FndInterfaceOperationRecordDTO;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.PortableInterceptor.ACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class AcOvertimeLeaveInterfaceServiceImplTest {

    @Autowired
    AcOvertimeLeaveInterfaceService acOvertimeLeaveInterfaceService;

    @Autowired
    AcOvertimeLeaveService acOvertimeLeaveService;

    @Autowired
    AcOvertimeLeaveInterfaceDao acOvertimeLeaveInterfaceDao;

    @Autowired
    AcOvertimeLeaveDao acOvertimeLeaveDao;

    @Autowired
    FndInterfaceOperationRecordService fndInterfaceOperationRecordService;

    @Autowired
    FndInterfaceOperationRecordDao fndInterfaceOperationRecordDao;

//    @Autowired


    @Test
    public void listAll() {
        AcOvertimeLeaveInterfaceQueryCriteria acOvertimeLeaveInterfaceQueryCriteria = new AcOvertimeLeaveInterfaceQueryCriteria();
        acOvertimeLeaveInterfaceQueryCriteria.setGroupId(1L);
        List<AcOvertimeLeaveInterface> acOvertimeLeaveInterfaceList = acOvertimeLeaveInterfaceDao.listAllByCriteria(acOvertimeLeaveInterfaceQueryCriteria);
        for (AcOvertimeLeaveInterface ac: acOvertimeLeaveInterfaceList
             ) {
            System.out.println(ac);
        }
    }

    @Test
    public void testCheckAndInsertInterFaceList() {
        AcOvertimeLeaveInterface acOvertimeLeaveInterface = new AcOvertimeLeaveInterface();
        acOvertimeLeaveInterface.setWorkCard("11");
        acOvertimeLeaveInterface.setGroupId(1L);
        acOvertimeLeaveInterface.setCreateBy(-1L);
        acOvertimeLeaveInterface.setCreateTime(LocalDateTime.now());
        acOvertimeLeaveInterface.setUpdateBy(-1L);
        acOvertimeLeaveInterface.setUpdateTime(LocalDateTime.now());
        acOvertimeLeaveInterface.setDateStr("1999.13.11");
        try {
            String[] strings = acOvertimeLeaveInterface.getDateStr().split(".");
            // 设置日期
            acOvertimeLeaveInterface.setDate(LocalDate.of(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), 1));
            if (strings[1].length() != 2) {
                acOvertimeLeaveInterface.setErrorMsg("日期格式错误，应为yyyy.MM。");
                acOvertimeLeaveInterface.setDataStatus("F");
            }
            if (strings[0].length() != 4) {
                acOvertimeLeaveInterface.setErrorMsg("日期格式错误，应为yyyy.MM。");
                acOvertimeLeaveInterface.setDataStatus("F");
            }
            acOvertimeLeaveInterface.setDate(LocalDate.of(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), 1));
        } catch (Exception e) {
            acOvertimeLeaveInterface.setErrorMsg("日期格式错误，应为yyyy.MM。");
            acOvertimeLeaveInterface.setDataStatus("F");
        }
        AcOvertimeLeaveInterface acOvertimeLeaveInterface2 = new AcOvertimeLeaveInterface();
        AcOvertimeLeaveInterface acOvertimeLeaveInterface3 = new AcOvertimeLeaveInterface();
        acOvertimeLeaveInterface2.setWorkCard("1019");
        acOvertimeLeaveInterface2.setGroupId(1L);
        acOvertimeLeaveInterface2.setCreateBy(-1L);
        acOvertimeLeaveInterface2.setCreateTime(LocalDateTime.now());
        acOvertimeLeaveInterface2.setUpdateBy(-1L);
        acOvertimeLeaveInterface2.setUpdateTime(LocalDateTime.now());
        acOvertimeLeaveInterface2.setAnnualLeave(BigDecimal.valueOf(10));

        acOvertimeLeaveInterface2.setBereavementLeave(BigDecimal.valueOf(12));
        acOvertimeLeaveInterface2.setWorkingDayOvertime(BigDecimal.valueOf(12));
        acOvertimeLeaveInterface2.setWorkRelatedInjuryLeave(BigDecimal.valueOf(13));

        acOvertimeLeaveInterface2.setCompassionateLeave(BigDecimal.valueOf(13));

        acOvertimeLeaveInterface2.setMarriageHoliday(BigDecimal.valueOf(14));

        acOvertimeLeaveInterface2.setMaternityLeave(BigDecimal.valueOf(11));

        acOvertimeLeaveInterface2.setOffHours(BigDecimal.valueOf(22));

        acOvertimeLeaveInterface2.setPaternityLeave(BigDecimal.valueOf(15));

        acOvertimeLeaveInterface2.setRestDayOvertime(BigDecimal.valueOf(33));

        acOvertimeLeaveInterface2.setSickLeave(BigDecimal.valueOf(11));
        acOvertimeLeaveInterface2.setDate(LocalDate.now());


        acOvertimeLeaveInterface3.setWorkCard("5556");
        acOvertimeLeaveInterface3.setGroupId(1L);
        acOvertimeLeaveInterface3.setCreateBy(-1L);
        acOvertimeLeaveInterface3.setCreateTime(LocalDateTime.now());
        acOvertimeLeaveInterface3.setUpdateBy(-1L);
        acOvertimeLeaveInterface3.setUpdateTime(LocalDateTime.now());
        acOvertimeLeaveInterface3.setAnnualLeave(BigDecimal.valueOf(10));

        acOvertimeLeaveInterface3.setBereavementLeave(BigDecimal.valueOf(12));
        acOvertimeLeaveInterface3.setWorkingDayOvertime(BigDecimal.valueOf(12));
        acOvertimeLeaveInterface3.setWorkRelatedInjuryLeave(BigDecimal.valueOf(13));

        acOvertimeLeaveInterface3.setCompassionateLeave(BigDecimal.valueOf(13));

        acOvertimeLeaveInterface3.setMarriageHoliday(BigDecimal.valueOf(14));

        acOvertimeLeaveInterface3.setMaternityLeave(BigDecimal.valueOf(11));

        acOvertimeLeaveInterface3.setOffHours(BigDecimal.valueOf(22));

        acOvertimeLeaveInterface3.setPaternityLeave(BigDecimal.valueOf(15));

        acOvertimeLeaveInterface3.setRestDayOvertime(BigDecimal.valueOf(33));

        acOvertimeLeaveInterface3.setSickLeave(BigDecimal.valueOf(11));
        acOvertimeLeaveInterface3.setDate(LocalDate.now());

        // 数据插入并匹配
        acOvertimeLeaveInterfaceDao.checkAndInsertInterFace(acOvertimeLeaveInterface);
        acOvertimeLeaveInterfaceDao.checkAndInsertInterFace(acOvertimeLeaveInterface2);
        acOvertimeLeaveInterfaceDao.checkAndInsertInterFace(acOvertimeLeaveInterface3);
    }

    @Test
    public void insertByInteface() {

        acOvertimeLeaveDao.deleteAllByMonth(LocalDate.now());

        acOvertimeLeaveDao.insertByInterface(1L);
    }



    @Test
    public void testInsertFndInterface() {
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
        fndInterfaceOperationRecord.setOperationDescription("员工加班请假统计导入");
        fndInterfaceOperationRecord.setOperationValue("AcOvertimeLeaveInterfaces");
        fndInterfaceOperationRecord.setSuccessFlag(true);
        FndInterfaceOperationRecordDTO fndInterfaceOperationRecordDTO = fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        System.out.println(fndInterfaceOperationRecordDTO.getId());
    }

    @Test
    public void testCul() {
      BigDecimal a =   BigDecimal.valueOf(3)
                .add(BigDecimal.valueOf(4))
                .add(BigDecimal.valueOf(5))
                .divide(BigDecimal.valueOf(2), 2, ROUND_HALF_UP)
                .subtract(BigDecimal.valueOf(0.1))
                .subtract(BigDecimal.valueOf(0.2))
                .subtract(BigDecimal.valueOf(0.3))
                .subtract(BigDecimal.valueOf(0.4))
                .subtract(BigDecimal.valueOf(0.5))
                .subtract(BigDecimal.valueOf(0.5))
                .subtract(BigDecimal.valueOf(0.6))
                .subtract(BigDecimal.valueOf(0.4))
                .subtract(BigDecimal.valueOf(0.5));
        System.out.println(a);
    }


}
