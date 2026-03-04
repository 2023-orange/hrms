package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.dao.AcCalendarHeaderDao;
import com.sunten.hrms.ac.dao.AcCalendarLineDao;
import com.sunten.hrms.ac.domain.AcCalendarHeader;
import com.sunten.hrms.ac.domain.AcCalendarLine;
import com.sunten.hrms.ac.dto.AcCalendarHeaderQueryCriteria;
import com.sunten.hrms.ac.service.AcCalendarHeaderService;
import com.sunten.hrms.ac.service.AcCalendarLineService;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* AcCalendarHeaderServiceImpl Tester.
*
* @author <Authors name>
* @since <pre>09/23/2020</pre>
* @version 1.0
*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class AcCalendarHeaderServiceImplTest {
    @Autowired
    AcCalendarHeaderService acCalendarHeaderService;
    @Autowired
    AcCalendarHeaderDao acCalendarHeaderDao;
    @Autowired
    AcCalendarLineService acCalendarLineService;
    @Autowired
    AcCalendarLineDao acCalendarLineDao;

@Before
public void before() throws Exception {
}

@After
public void after() throws Exception {
}

/**
*
* Method: insert(AcCalendarHeader calendarHeaderNew)
*
*/
@Test
public void testInsert() throws Exception {
//TODO: Test goes here...

//    AcCalendarHeader acCalendarHeader = new AcCalendarHeader();
//    acCalendarHeader.setCalendarName("测试日历新增");
//    acCalendarHeader.setDefaultFlag(true);
//    acCalendarHeader.setEnabledFlag(true);
//    if (acCalendarHeader.getDefaultFlag()) {
//        AcCalendarHeaderQueryCriteria acCalendarHeaderQueryCriteria = new AcCalendarHeaderQueryCriteria();
//        acCalendarHeaderQueryCriteria.setDefaultFlag(true);
//        acCalendarHeaderQueryCriteria.setEnabled(true);
//        List<AcCalendarHeader> acCalendarHeaders = acCalendarHeaderDao.listAllByCriteria(acCalendarHeaderQueryCriteria);
//        if (acCalendarHeaders.size() > 0) {
//            AcCalendarHeader changeAcCalendarHeader = acCalendarHeaders.get(0);
//            changeAcCalendarHeader.setDefaultFlag(false);
//            acCalendarHeaderDao.updateAllColumnByKey(changeAcCalendarHeader);
//        }
//    }
//    acCalendarHeaderDao.insertAllColumn(acCalendarHeader);
    // 生成日历行
    LocalDate now = LocalDate.now();
    List<AcCalendarLine> acCalendarLines =  acCalendarLineService.generateAcCalendarLines(now, 1L);
    // 批量插入时最多可插入2100个参数，分批插入，分三批插入，每批130条
    List<AcCalendarLine> acCalendarLines1 = acCalendarLines.stream().filter(AcCalendarLine -> AcCalendarLine.getOrder() <= 130).collect(Collectors.toList());
    acCalendarLineDao.insertCollection(acCalendarLines1);
    List<AcCalendarLine> acCalendarLines2 = acCalendarLines.stream().filter(AcCalendarLine -> AcCalendarLine.getOrder() <= 260 && AcCalendarLine.getOrder() >130).collect(Collectors.toList());
    acCalendarLineDao.insertCollection(acCalendarLines2);
    List<AcCalendarLine> acCalendarLines3 = acCalendarLines.stream().filter(AcCalendarLine -> AcCalendarLine.getOrder() > 260).collect(Collectors.toList());
    acCalendarLineDao.insertCollection(acCalendarLines3);
}

/**
*
* Method: delete(Long id)
*
*/
@Test
public void testDeleteId() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: delete(AcCalendarHeader calendarHeader)
*
*/
@Test
public void testDeleteCalendarHeader() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: update(AcCalendarHeader calendarHeaderNew)
*
*/
@Test
public void testUpdate() throws Exception {
//TODO: Test goes here...
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
* Method: listAll(AcCalendarHeaderQueryCriteria criteria)
*
*/
@Test
public void testListAllCriteria() throws Exception {
//TODO: Test goes here...
    AcCalendarHeaderQueryCriteria acCalendarHeaderQueryCriteria = new AcCalendarHeaderQueryCriteria();
//    acCalendarHeaderQueryCriteria.setEnabled(true);
    acCalendarHeaderQueryCriteria.setDefaultFlag(false);
    List<AcCalendarHeader> acCalendarHeaders = acCalendarHeaderDao.listAllByCriteria(acCalendarHeaderQueryCriteria);
    System.out.println(acCalendarHeaders.get(0));
    System.out.println(acCalendarHeaders.size());
}

/**
*
* Method: listAll(AcCalendarHeaderQueryCriteria criteria, Pageable pageable)
*
*/
@Test
public void testListAllForCriteriaPageable() throws Exception {
//TODO: Test goes here...
    Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
    Pageable pageable = PageRequest.of(0, 60, sort);
    AcCalendarHeaderQueryCriteria acCalendarHeaderQueryCriteria = new AcCalendarHeaderQueryCriteria();
    acCalendarHeaderQueryCriteria.setEnabled(true);
    acCalendarHeaderQueryCriteria.setId(1L);
    Map<String,Object> acCalendarHeaders = acCalendarHeaderService.listAll(acCalendarHeaderQueryCriteria, pageable);
    System.out.println(acCalendarHeaders.toString());
    System.out.println(acCalendarHeaders.get("content"));
}

/**
*
* Method: download(List<AcCalendarHeaderDTO> calendarHeaderDTOS, HttpServletResponse response)
*
*/
@Test
public void testDownload() throws Exception {
//TODO: Test goes here...
}


}
