package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcLeaveForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class AcLeaveFormDaoTest {
    @Autowired
    AcLeaveFormDao acLeaveFormDao;

    @Test
    public void getLeaveFormByDate() {
        List<AcLeaveForm> acLeaveForms = acLeaveFormDao.getLeaveFormByDate(LocalDate.now().minusDays(150),LocalDate.now());
        for (AcLeaveForm ac: acLeaveForms
             ) {
            System.out.println(ac);
        }
    }
}
