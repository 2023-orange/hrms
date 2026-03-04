package com.sunten.erp.fnd.dao;

import com.sunten.erp.fnd.domain.ErpFndUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ErpFndUserDaoTest {
    @Autowired
    ErpFndUserDao erpFndUserDao;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void selectAll() {
        List<ErpFndUser> aa = erpFndUserDao.selectFndUserByName("MRPTBA");
        for(ErpFndUser a:aa){
            System.out.println(a);
        }
    }
}