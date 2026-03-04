package com.sunten.erp.fnd.service.impl;

import com.sunten.erp.fnd.domain.TbaTest;
import com.sunten.erp.fnd.service.TbaTestService;
import com.sunten.erp.fnd.service.TransactionService;
import com.sunten.hrms.fnd.domain.FndDept;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TbaTestServiceImplTest {
    @Autowired
    TbaTestService tbaTestService;

    @Autowired
    TransactionService transactionService;

    @Test
    public void insert() {
        TbaTest tbaTest = new TbaTest();
        tbaTest.setId((long) 2).setDescription("测试");
        tbaTestService.insert(tbaTest);
        System.out.println(tbaTest);
    }

    @Test
    public void insertTransaction() {
        TbaTest tbaTest = new TbaTest();
        tbaTest.setId((long) 6).setDescription("测试");
        TbaTest tbaTest1 = new TbaTest();
        tbaTest1.setId((long) 4).setDescription("测试");
        List<TbaTest> tbaTestList = new ArrayList<>();
        tbaTestList.add(tbaTest);
        tbaTestList.add(tbaTest1);
        tbaTestService.insert(tbaTestList);
    }


    @Test
    public void transaction() {
        TbaTest tbaTest = new TbaTest();
        tbaTest.setId((long) 80).setDescription("测试");
        TbaTest tbaTest1 = new TbaTest();
        tbaTest1.setId((long) 81).setDescription("测试");
        FndDept dept1 = new FndDept();
        dept1.setDeptName("测试5").setParentId(1L).setEnabledFlag(true).setDeptCode("测试4").setDeptLevel("部").setDeptSequence(1L).setDeletedFlag(true);
//        dept1.setCreateBy(-1L);
//        dept1.setCreateTime(LocalDateTime.now());
//        dept1.setUpdateBy(-1L);
//        dept1.setUpdateTime(LocalDateTime.now());
        FndDept dept2 = new FndDept();
        dept2.setDeptName("测试6").setParentId(1L).setEnabledFlag(true).setDeptCode("测试5")
                .setDeptLevel("部").setDeptSequence(1L).setDeletedFlag(true);
        dept2.setCreateBy(-1L);
        dept2.setCreateTime(LocalDateTime.now());
        dept2.setUpdateBy(-1L);
        dept2.setUpdateTime(LocalDateTime.now());
        List<TbaTest> tbaTestList = new ArrayList<>();
        List<FndDept> depts = new ArrayList<>();
        tbaTestList.add(tbaTest);
        tbaTestList.add(tbaTest1);
        depts.add(dept1);
        depts.add(dept2);
        tbaTestService.transaction(tbaTestList, depts);
    }


    @Test
    public void listAll() {
        Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(0, 10, sort);
        List<TbaTest> tests = tbaTestService.listAll(pageable);
        tests.forEach(tbaTest -> {
            System.out.println(tbaTest);
        });
    }
}
