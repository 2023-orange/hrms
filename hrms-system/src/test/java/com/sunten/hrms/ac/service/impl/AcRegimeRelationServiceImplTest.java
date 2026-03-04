package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.dao.AcRegimeDao;
import com.sunten.hrms.ac.dao.AcRegimeRelationDao;
import com.sunten.hrms.ac.domain.AcRegime;
import com.sunten.hrms.ac.domain.AcRegimeRelation;
import com.sunten.hrms.ac.domain.AcRegimeTime;
import com.sunten.hrms.ac.dto.AcRegimeQueryCriteria;
import com.sunten.hrms.ac.dto.AcRegimeRelationDTO;
import com.sunten.hrms.ac.dto.AcRegimeRelationQueryCriteria;
import com.sunten.hrms.ac.mapper.AcRegimeRelationMapper;
import com.sunten.hrms.ac.service.AcRegimeRelationService;
import com.sunten.hrms.ac.service.AcRegimeService;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
* AcRegimeRelationServiceImpl Tester.
*
* @author <Authors name>
* @since <pre>09/18/2020</pre>
* @version 1.0
*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class AcRegimeRelationServiceImplTest {
@Autowired
AcRegimeRelationServiceImpl acRegimeRelationServiceImpl;
    @Autowired
    AcRegimeRelationMapper acRegimeRelationMapper;
    @Autowired
    AcRegimeRelationService acRegimeRelationService;
    @Autowired
    AcRegimeRelationDao acRegimeRelationDao;
    @Autowired
    AcRegimeService acRegimeService;
    @Autowired
    AcRegimeDao acRegimeDao;
@Before
public void before() throws Exception {
}

@After
public void after() throws Exception {
}

/**
*
* Method: insert(AcRegimeRelation regimeRelationNew)
*
*/
@Test
public void testInsert() throws Exception {
//TODO: Test goes here...
    AcRegimeRelation acRegimeRelation = new AcRegimeRelation();
    acRegimeRelation.setEnabledFlag(true);
    acRegimeRelation.setRegimeaId(1L);
    acRegimeRelation.setRegimeTimeId(1L);
    System.out.println(acRegimeRelationServiceImpl.insert(acRegimeRelation));
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
* Method: delete(AcRegimeRelation regimeRelation)
*
*/
@Transactional
@Test
public void testDeleteRegimeRelation() throws Exception {
//TODO: Test goes here...
    // 测试批量删除
    AcRegimeQueryCriteria acRegimeQueryCriteria = new AcRegimeQueryCriteria();
    acRegimeQueryCriteria.setArId(5L);
    List<AcRegime> ac = acRegimeDao.listAllByCriteria(acRegimeQueryCriteria);
    System.out.println(ac.size());
    acRegimeRelationDao.deleteCollection(ac.get(0).getAcRegimeRelations());
//    //测试事务回滚是否有效
//    AcRegimeRelation acRegimeRelation = new AcRegimeRelation();
//    acRegimeRelation.setRegimeaId(1L);
//    acRegimeRelation.setRegimeTimeId(1L);
//    acRegimeRelationServiceImpl.insert(acRegimeRelation);
}

/**
*
* Method: update(AcRegimeRelation regimeRelationNew)
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
* Method: listAll(AcRegimeRelationQueryCriteria criteria)
*
*/
@Test
public void testListAllCriteria() throws Exception {
//TODO: Test goes here...
    AcRegimeRelationQueryCriteria acRegimeRelationQueryCriteria = new AcRegimeRelationQueryCriteria();
    acRegimeRelationQueryCriteria.setRegimeTimeId(1L);
    acRegimeRelationQueryCriteria.setEnabled(true);
    List<AcRegimeRelationDTO> acRegimeRelationDTOList = acRegimeRelationService.listAll(acRegimeRelationQueryCriteria);
    System.out.println(acRegimeRelationDTOList);
    List<AcRegimeRelation> acRegimeRelationDTOLis = acRegimeRelationDao.listAllByCriteria(acRegimeRelationQueryCriteria);
    System.out.println(acRegimeRelationDTOLis);
}

/**
*
* Method: listAll(AcRegimeRelationQueryCriteria criteria, Pageable pageable)
*
*/
@Test
public void testListAllForCriteriaPageable() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: download(List<AcRegimeRelationDTO> regimeRelationDTOS, HttpServletResponse response)
*
*/
@Test
public void testDownload() throws Exception {
//TODO: Test goes here...
}


}
