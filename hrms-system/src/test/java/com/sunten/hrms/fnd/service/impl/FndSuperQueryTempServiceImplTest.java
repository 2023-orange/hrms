package com.sunten.hrms.fnd.service.impl;

import com.sunten.hrms.fnd.dto.FndSuperQueryTempQueryCriteria;
import com.sunten.hrms.fnd.service.FndSuperQueryTempService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FndSuperQueryTempServiceImplTest {
    @Autowired
    FndSuperQueryTempService fndSuperQueryTempService;

    @Test
    public void createTemp() {
        FndSuperQueryTempQueryCriteria criteria = new FndSuperQueryTempQueryCriteria();
        criteria.setGroupName("personnel_management");
        criteria.setQueryValue("2");
        criteria.setSearchUserId(1L);
        criteria.setCreateFlag(true);
//        fndSuperQueryTempService.createTemp(criteria);
    }
}
