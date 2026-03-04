package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeJobSnapshot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PmEmployeeJobSnapshotDaoTest {

    @Autowired
    PmEmployeeJobSnapshotDao pmEmployeeJobSnapshotDao;


    @Test
    public void listEmpAttendanceByDate() {
        List<PmEmployeeJobSnapshot> pmEmployeeJobSnapshotList = pmEmployeeJobSnapshotDao.listAllWithAttendanceByDate(LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));
        for (PmEmployeeJobSnapshot pjs : pmEmployeeJobSnapshotList
             ) {
            System.out.println(pjs);
        }
    }

    @Test
    public void generateSnapShot() {
        // 生成快照

        for (int i = 10; i < 30; i++) {
            PmEmployeeJobSnapshot pmEmployeeJobSnapshot = new PmEmployeeJobSnapshot();
            pmEmployeeJobSnapshot.setDate(LocalDate.of(2020,10,i)); // 20 ,21
            pmEmployeeJobSnapshot.setUpdateTime(LocalDateTime.now());
            pmEmployeeJobSnapshot.setCreateTime(LocalDateTime.now());
            pmEmployeeJobSnapshot.setCreateBy(-1L);
            pmEmployeeJobSnapshot.setUpdateBy(-1L);
            pmEmployeeJobSnapshotDao.insertSnapShot(pmEmployeeJobSnapshot);
        }
    }
}
