package com.sunten.erp.fnd.dao;

import com.sunten.erp.fnd.domain.TbaTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TbaTestDaoTest {
    @Autowired
    TbaTestDao tbaTestDao;

    @Test
    public void insertAllColumn() {
        TbaTest tbaTest = new TbaTest();
        tbaTest.setId((long)1).setDescription("测试");
        tbaTestDao.insert(tbaTest);
    }
}