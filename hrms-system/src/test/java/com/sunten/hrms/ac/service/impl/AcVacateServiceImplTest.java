package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.dto.AcOvertimeQueryCriteria;
import com.sunten.hrms.ac.dto.AcVacateDTO;
import com.sunten.hrms.ac.service.AcOvertimeService;
import com.sunten.hrms.ac.service.AcVacateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @atuthor xukai
 * @date 2020/10/15 13:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AcVacateServiceImplTest {

    @Autowired
    AcVacateService acVacateService;

    @Autowired
    AcOvertimeService acOvertimeService;

    @Test
    public void testContextOA(){
        System.out.println("开始测试");
        AcVacateDTO acVacateDTO = acVacateService.getVacateByRequisitionCode("1809270224428");
        System.out.println(acVacateDTO);
    }

    @Test
    public void testOveritmeList() {
        AcOvertimeQueryCriteria criteria = new AcOvertimeQueryCriteria();
        criteria.setWorkCode("4429");
        Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(0, 10, sort);
        Map<String, Object> map = acOvertimeService.listAll(criteria,pageable);
    }
}
