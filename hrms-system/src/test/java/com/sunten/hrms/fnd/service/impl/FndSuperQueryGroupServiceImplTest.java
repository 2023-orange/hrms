package com.sunten.hrms.fnd.service.impl;

import com.sunten.hrms.fnd.domain.FndSuperQueryGroup;
import com.sunten.hrms.fnd.dto.FndSuperQueryGroupQueryCriteria;
import com.sunten.hrms.fnd.service.FndSuperQueryGroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FndSuperQueryGroupServiceImplTest {
    @Autowired
    FndSuperQueryGroupService fndSuperQueryGroupService;

    @Test
    public void listAllExpand() {
        FndSuperQueryGroupQueryCriteria criteria = new FndSuperQueryGroupQueryCriteria();
        criteria.setEnabledFlag(true);
        criteria.setGroupName("personnel_management");
        List<FndSuperQueryGroup> groups = fndSuperQueryGroupService.listAllExpand(criteria);
        System.out.println(groups);
    }
}
