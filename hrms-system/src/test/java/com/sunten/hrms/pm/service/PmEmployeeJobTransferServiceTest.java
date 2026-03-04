package com.sunten.hrms.pm.service;

import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.domain.FndJob;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeJobTransfer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PmEmployeeJobTransferServiceTest {
    @Autowired
    PmEmployeeJobTransferService pmEmployeeJobTransferService;

    @Test
    public void insert() {
        PmEmployeeJobTransfer transfer = new PmEmployeeJobTransfer();
        PmEmployee employee = new PmEmployee();
        employee.setId(100L);
        transfer.setEmployee(employee);
        transfer.setGroupId(172L);
        FndJob oldJob = new FndJob();
        oldJob.setId(129L);
        oldJob.setJobName("班长");

        FndDept oldDept = new FndDept();
        oldDept.setId(137L);
        oldDept.setDeptName("计量站");

        FndJob newJob = new FndJob();
        newJob.setId(26L);
        newJob.setJobName("ERP系统技术支持员");

        FndDept newDept = new FndDept();
        newDept.setId(87L);
        newDept.setDeptName("系统应用室");
//
//        transfer.setOldJob(oldJob);
//        transfer.setOldDept(oldDept);
//        transfer.setNewJob(newJob);
//        transfer.setNewDept(newDept);

        transfer.setOldJob(newJob);
        transfer.setOldDept(newDept);
        transfer.setNewJob(oldJob);
        transfer.setNewDept(oldDept);


        transfer.setStartTime(LocalDate.now().minusDays(10));
        transfer.setEndTime(LocalDate.now().minusDays(3));

        pmEmployeeJobTransferService.insert(transfer);
        System.out.println(transfer);


    }


    @Test
    public void isPlanOrTransferring() {
        System.out.println(pmEmployeeJobTransferService.isPlanOrTransferring(100L, 100L));
    }

    @Test
    public void updateEndTime() {
        PmEmployeeJobTransfer transfer = new PmEmployeeJobTransfer();

        transfer.setId(156L);

//        transfer.setEndTime();
        transfer.setEndTime(LocalDate.now().plusDays(1));
        pmEmployeeJobTransferService.updateEndTime(transfer);
    }


    @Test
    public void delete() {
        pmEmployeeJobTransferService.delete(157L);
    }

    @Test
    public void fini() {
        pmEmployeeJobTransferService.finishTransfer(LocalDate.now().plusDays(11));
    }


}
