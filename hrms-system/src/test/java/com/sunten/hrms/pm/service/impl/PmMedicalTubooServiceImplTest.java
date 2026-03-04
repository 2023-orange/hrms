package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.pm.dto.PmMedicalTubooQueryCriteria;
import com.sunten.hrms.pm.service.PmMedicalTubooService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.ws.soap.Addressing;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PmMedicalTubooServiceImplTest {
    @Autowired
    private PmMedicalTubooService pmMedicalTubooService;

    @Test
    public void listAll() {
    }

    @Test
    public void testListAll() {
        PmMedicalTubooQueryCriteria criteria = new PmMedicalTubooQueryCriteria();
        Sort sort = Sort.by("id");
        Pageable pageable = PageRequest.of(0, 10, sort);
        pmMedicalTubooService.listAll(criteria, pageable);
    }
}