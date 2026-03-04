package com.sunten.erp.cux.dao;

import com.sunten.erp.cux.domain.CuxTestTransaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CuxTestTransactionDaoTest {
    @Autowired
    CuxTestTransactionDao cuxTestTransactionDao;

    @Test
    public void insertAllColumn() {
        CuxTestTransaction testTransaction = new CuxTestTransaction();
        testTransaction.setId(new BigDecimal(2));
        testTransaction.setDescription("测试");
        testTransaction.setCreationDate(LocalDateTime.now());
        cuxTestTransactionDao.insertAllColumn(testTransaction);

    }

    @Test
    public void listAll() {
        List<CuxTestTransaction> cuxTestTransactionList = cuxTestTransactionDao.listAll();
        for (CuxTestTransaction testTransaction :
                cuxTestTransactionList) {
            System.out.println(testTransaction);
        }
    }
}