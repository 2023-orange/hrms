package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.domain.AcOvertimeLeave;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveDTO;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveQueryCriteria;
import com.sunten.hrms.ac.service.AcOvertimeLeaveService;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.fnd.domain.FndDept;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AcOvertimeLeaveServiceImplTest {
    @Autowired
    AcOvertimeLeaveService acOvertimeLeaveService;

    @Autowired
    FndDataScope fndDataScope;

    @Test
    public void testList() {
        AcOvertimeLeaveQueryCriteria queryCriteria = new AcOvertimeLeaveQueryCriteria();
        queryCriteria.setDeptId(1L);
        setDeptIds(queryCriteria);
        List<AcOvertimeLeaveDTO> acOvertimeLeaves = acOvertimeLeaveService.listAll(queryCriteria);
        for (AcOvertimeLeaveDTO ac: acOvertimeLeaves
             ) {
            System.out.println(ac);
        }
    }


    private void setDeptIds(AcOvertimeLeaveQueryCriteria criteria) {
        if (null != criteria.getDeptId()) {
            // 获取deptIds
            FndDept fndDept = new FndDept();
            fndDept.setEnabledFlag(true);
            fndDept.setId(criteria.getDeptId());
            List<FndDept> fndDepts = new ArrayList<>();
            fndDepts.add(fndDept);
//            List<Long> deptIds = fndDataScope.getDeptChildren(fndDepts);
//            criteria.setDeptIds(deptIds);
        }
    }

}
