package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.ac.domain.AcDeptAttendance;
import com.sunten.hrms.ac.service.impl.AcDeptAttendanceServiceImpl;
import com.sunten.hrms.fnd.domain.FndDeptSnapshot;
import com.sunten.hrms.fnd.dto.FndDeptSnapshotQueryCriteria;
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
public class FndDeptSnapshotDaoTest {
    @Autowired
    FndDeptSnapshotDao fndDeptSnapshotDao;

    @Autowired
    AcDeptAttendanceServiceImpl acDeptAttendanceServiceImpl;

    @Test
    public void testList(){

        List<FndDeptSnapshot> fndDeptSnapshots = fndDeptSnapshotDao.listAllWithAttendanceByCriteria(LocalDate.now().minusDays(1));
        for(FndDeptSnapshot fndDeptSnapshot:fndDeptSnapshots){
            if(fndDeptSnapshot.getAttendance() == null){
                fndDeptSnapshot.setAttendance(getParentAttendance(fndDeptSnapshot, fndDeptSnapshots));
            }
        }
        for(FndDeptSnapshot fndDeptSnapshot:fndDeptSnapshots){
            System.out.println(fndDeptSnapshot.getDeptId() + "=============" + fndDeptSnapshot.getDeptName() + "=========="
                    + fndDeptSnapshot.getAttendance().getDeptId() + "++++" + fndDeptSnapshot.getAttendance().getRegimeId()) ;
        }


    }
    public AcDeptAttendance getParentAttendance (FndDeptSnapshot current, List<FndDeptSnapshot> fndDeptSnapshots) {
        //找parent
        for (FndDeptSnapshot fndDeptSnapshot : fndDeptSnapshots ){

            if (current.getParentId().equals(fndDeptSnapshot.getDeptId())) {

                if(fndDeptSnapshot.getAttendance() != null){
                    return fndDeptSnapshot.getAttendance();
                } else {
                    if(fndDeptSnapshot.getParentId() != null){
                        fndDeptSnapshot.setAttendance(getParentAttendance(fndDeptSnapshot, fndDeptSnapshots));
                        return fndDeptSnapshot.getAttendance();
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;


    }

    @Test
    public void generateSnapShot() {
        for (int i = 10; i < 30; i++) {
            FndDeptSnapshot fndDeptSnapshot = new FndDeptSnapshot();
            fndDeptSnapshot.setDate(LocalDate.of(2020,10,i));
            fndDeptSnapshot.setCreateBy(-1L);
            fndDeptSnapshot.setUpdateBy(-1L);
            fndDeptSnapshot.setCreateTime(LocalDateTime.now());
            fndDeptSnapshot.setUpdateTime(LocalDateTime.now());
            fndDeptSnapshotDao.insertSnapShot(fndDeptSnapshot);
            List<FndDeptSnapshot> fndDeptSnapshots = fndDeptSnapshotDao.listAllWithAttendanceByCriteria(LocalDate.of(2020,10,i));
            for (FndDeptSnapshot fds : fndDeptSnapshots) {
                if (fds.getAttendance() == null) {
                    fds.setAttendance(acDeptAttendanceServiceImpl.getParentAttendance(fds, fndDeptSnapshots));
                }
            }
            for (FndDeptSnapshot fds : fndDeptSnapshots) {
                fds.setUpdateTime(LocalDateTime.now());
                fds.setUpdateBy(-1L);
                fds.setAttendanceId(fds.getAttendance().getId());
                fndDeptSnapshotDao.updateAttendanceId(fds);
            }
        }
    }

}



