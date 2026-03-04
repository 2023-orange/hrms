package com.sunten.hrms.fnd.service.impl;

import com.sunten.hrms.fnd.domain.FndUpdateHistory;
import com.sunten.hrms.fnd.dto.FndUpdateHistoryDTO;
import com.sunten.hrms.fnd.dto.FndUpdateHistoryQueryCriteria;
import com.sunten.hrms.fnd.service.FndJobService;
import com.sunten.hrms.fnd.service.FndUpdateHistoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FndJobServiceImplTest {
    @Autowired
    FndJobService fndJobService;
    @Autowired
    FndUpdateHistoryService fndUpdateHistoryService;

    @Test
    public void insert() {
    }

    @Test
    public void delete() {
        Long key = (long)20;
        System.out.println("====================before delete");
        System.out.println(fndJobService.getByKey(key));
        List<FndUpdateHistoryDTO> updateHistoryDTOS = fndUpdateHistoryService.listAll(new FndUpdateHistoryQueryCriteria());
        for(FndUpdateHistoryDTO a:updateHistoryDTOS){
            System.out.println(a);
        }
        fndJobService.delete(key);
        System.out.println("====================after delete");
        System.out.println(fndJobService.getByKey(key));
        List<FndUpdateHistoryDTO> updateHistoryDTOS1 = fndUpdateHistoryService.listAll(new FndUpdateHistoryQueryCriteria());
        for(FndUpdateHistoryDTO a:updateHistoryDTOS1){
            System.out.println(a);
        }
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
