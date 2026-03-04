package com.sunten.hrms.pm.service;

import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @atuthor xukai
 * @date 2020/8/7 14:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PMEmployeeJobServiceTest {
    @Autowired
    PmEmployeeJobService pmEmployeeJobService;

    @Test
    void ListAllTest(){
//        PmEmployeeQueryCriteria employeeQueryCriteria = new PmEmployeeQueryCriteria();//参数
//        //部门
//        FndDept fndDept = new FndDept();
//        fndDept.setId(1L);
//        employeeQueryCriteria.setDept(fndDept);
//        //查询参数
//        employeeQueryCriteria.setEnabledFlag(true);
//        employeeQueryCriteria.setColumnName("name");
//        employeeQueryCriteria.setSɪmbl("=");
//        employeeQueryCriteria.setValue("徐凯");
//
//        List<PmEmployeeDTO> employeeDTOS =  pmEmployeeJobService.listAll(employeeQueryCriteria);
    }
}
