package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.dao.AcRegimeTimeDao;
import com.sunten.hrms.ac.domain.AcRegimeTime;
import com.sunten.hrms.ac.dto.AcRegimeTimeDTO;
import com.sunten.hrms.ac.dto.AcRegimeTimeQueryCriteria;
import com.sunten.hrms.ac.mapper.AcRegimeTimeMapper;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
* AcRegimeTimeServiceImpl Tester.
*
* @author <Authors name>
* @since <pre>09/18/2020</pre>
* @version 1.0
*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class AcRegimeTimeServiceImplTest {
    @Autowired
    AcRegimeTimeServiceImpl acRegimeTimeServiceImpl;
    @Autowired
    AcRegimeTimeMapper acRegimeTimeMapper;
    @Autowired
    AcRegimeTimeDao acRegimeTimeDao;
    @Before
public void before() throws Exception {
}

@After
public void after() throws Exception {
}

/**
*
* Method: insert(AcRegimeTime regimeTimeNew)
*
*/
@Test
public void testInsert() throws Exception {
//TODO: Test goes here...
    AcRegimeTime acRegimeTime = new AcRegimeTime();
    acRegimeTime.setTimeFrom(LocalTime.now());
    acRegimeTime.setTimeTo(LocalTime.now());
    acRegimeTime.setExtendTimeFlag(false);
    acRegimeTime.setEnabledFlag(true);
    System.out.println(acRegimeTimeServiceImpl.insert(acRegimeTime));
}

/**
*
* Method: delete(Long id)
*
*/
@Test
public void testDeleteId() throws Exception {
//TODO: Test goes here...
    AcRegimeTime acRegimeTime = new AcRegimeTime();
    acRegimeTime.setId(10L);
    acRegimeTime.setEnabledFlag(false);
    acRegimeTimeDao.deleteByEnabled(acRegimeTime);
}

/**
*
* Method: delete(AcRegimeTime regimeTime)
*
*/
@Test
public void testDeleteRegimeTime() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: update(AcRegimeTime regimeTimeNew)
*
*/
@Test
public void testUpdate() throws Exception {
//TODO: Test goes here..
    AcRegimeTime acRegimeTime = new AcRegimeTime();
    acRegimeTime.setId(2L);
    acRegimeTime.setEnabledFlag(true);
    acRegimeTime.setTimeTo(LocalTime.now());
    acRegimeTime.setTimeFrom(LocalTime.now());
    acRegimeTime.setExtendTimeFlag(false);
    acRegimeTimeServiceImpl.update(acRegimeTime);
}

/**
*
* Method: getByKey(Long id)
*
*/
@Test
public void testGetByKey() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: listAll(AcRegimeTimeQueryCriteria criteria)
*
*/
@Test
public void testListAllCriteria() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: listAll(AcRegimeTimeQueryCriteria criteria, Pageable pageable)
*
*/
@Test
public void testListAllForCriteriaPageable() throws Exception {
    Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
    Pageable pageable = PageRequest.of(0, 10, sort);
    AcRegimeTimeQueryCriteria acRegimeTimeQueryCriteria = new AcRegimeTimeQueryCriteria();
    acRegimeTimeQueryCriteria.setEnabled(true);
    Map<String, Object> acRegimeTimeDTOList = acRegimeTimeServiceImpl.listAll(acRegimeTimeQueryCriteria,pageable);
    System.out.println(acRegimeTimeDTOList.toString());
    //TODO: Test goes here...
}

/**
*
* Method: download(List<AcRegimeTimeDTO> regimeTimeDTOS, HttpServletResponse response)
*
*/
@Test
public void testDownload() throws Exception {
//TODO: Test goes here...
}


}
