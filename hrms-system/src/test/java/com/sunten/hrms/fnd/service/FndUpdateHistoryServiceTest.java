package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndUpdateHistory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FndUpdateHistoryServiceTest {
    @Autowired
    FndUpdateHistoryService fndUpdateHistoryService;

    @Test
    public void insert() {
        FndUpdateHistory updateHistory = new FndUpdateHistory();
        updateHistory.setTableName("testTableName");
        updateHistory.setColumnName("testColumnName");
        updateHistory.setTableId((long)1);
        updateHistory.setNewValue("newValue");
        updateHistory.setOldValue("oldValue");
        System.out.println("============begin insert");
        System.out.println(updateHistory);
        fndUpdateHistoryService.insert(updateHistory);
        System.out.println("============after insert");
        System.out.println(updateHistory);
    }

    @Test
    public void delete() {
    }

    @Test
    public void testDelete() {
    }

    @Test
    public void update() {
    }

    @Test
    public void getByKey() {
    }

    @Test
    public void listAll() {
    }

    @Test
    public void testListAll() {
    }

    @Test
    public void download() {
    }
}
