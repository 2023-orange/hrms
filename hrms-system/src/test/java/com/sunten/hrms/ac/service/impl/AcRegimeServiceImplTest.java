package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.ac.dao.AcRegimeDao;
import com.sunten.hrms.ac.dao.AcRegimeRelationDao;
import com.sunten.hrms.ac.dao.AcRegimeTimeDao;
import com.sunten.hrms.ac.domain.AcRegime;
import com.sunten.hrms.ac.domain.AcRegimeRelation;
import com.sunten.hrms.ac.domain.AcRegimeTime;
import com.sunten.hrms.ac.dto.AcRegimeDTO;
import com.sunten.hrms.ac.dto.AcRegimeQueryCriteria;
import com.sunten.hrms.ac.dto.AcRegimeRelationDTO;
import com.sunten.hrms.ac.mapper.AcRegimeMapper;
import com.sunten.hrms.ac.service.AcRegimeRelationService;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.apache.tomcat.jni.Local;
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
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* AcRegimeServiceImpl Tester.
*
* @author <Authors name>
* @since <pre>09/18/2020</pre>
* @version 1.0
*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class AcRegimeServiceImplTest {
    @Autowired
    AcRegimeServiceImpl acRegimeServiceImpl;
    @Autowired
    AcRegimeDao acRegimeDao;
    @Autowired
    AcRegimeMapper acRegimeMapper;
    @Autowired
    AcRegimeRelationDao acRegimeRelationDao;
    @Autowired
    AcRegimeRelationService acRegimeRelationService;

@Before
public void before() throws Exception {
}

@After
public void after() throws Exception {
}

/**
*
* Method: insert(AcRegime regimeNew)
*
*/
@Test
public void testInsert() throws Exception {
//TODO: Test goes here...
    AcRegime acRegime = new AcRegime();
    acRegime.setRegimeName("测试DruidConfig");
    acRegime.setRegimeCode("DruidConfig");
    acRegime.setEnabledFlag(true);
    AcRegimeDTO acRegimeDTO = acRegimeServiceImpl.insert(acRegime);
    System.out.println("======新插入的考勤id======");
    System.out.println(acRegimeDTO.getId());
    List<AcRegimeRelation> regimeRelations = new ArrayList<>();
    AcRegimeRelation acRegimeRelation1 = new AcRegimeRelation();
    AcRegimeRelation acRegimeRelation2 = new AcRegimeRelation();
    acRegimeRelation1.setRegimeaId(acRegimeDTO.getId());
    acRegimeRelation1.setEnabledFlag(true);
    acRegimeRelation1.setRegimeTimeId(3L);
    acRegimeRelation2.setRegimeaId(acRegimeDTO.getId());
    acRegimeRelation2.setEnabledFlag(true);
    acRegimeRelation2.setRegimeTimeId(4L);
    regimeRelations.add(acRegimeRelation1);
    regimeRelations.add(acRegimeRelation2);
    acRegime.setAcRegimeRelations(regimeRelations);
    if (acRegime.getAcRegimeRelations().size() > 0) {
        System.out.println("----------输出循环插入结果------");
        System.out.println(acRegimeRelationDao.insertCollection(acRegime.getAcRegimeRelations()));
    }
}

/**
*
* Method: delete(Long id)
*
*/
@Test
public void testDeleteId() throws Exception {
//TODO: Test goes here...
    LocalDate now = LocalDate.now();
    LocalDate now1 = LocalDate.now().plusDays(1);
    LocalDate now2 = LocalDate.now().plusDays(2);
    List<LocalDate> dateList = new ArrayList<>();
    dateList.add(now);
    dateList.add(now1);
    dateList.add(now2);
    System.out.println(dateList);
    dateList.stream().filter(LocalDate -> LocalDate.getDayOfMonth() == LocalDate.getDayOfMonth()).collect(Collectors.toList());
    System.out.println(dateList);

//    AcRegimeQueryCriteria acRegimeQueryCriteria = new AcRegimeQueryCriteria();
//    acRegimeQueryCriteria.setEnabled(true);
//    acRegimeQueryCriteria.setArId(3L);
//    List<AcRegimeDTO> acRegimes = acRegimeServiceImpl.listAll(acRegimeQueryCriteria);
//    System.out.println("删除主");
//    AcRegime acRegime = new AcRegime();
//    acRegime.setId(3L);
//    System.out.println(acRegimeDao.deleteByEnabled(acRegime));
//    System.out.println("删除关系");
//    List<AcRegimeRelationDTO> acRegimeRelationList = acRegimes.get(0).getAcRegimeRelations();
//    System.out.println(acRegimeRelationList);
//    for (AcRegimeRelationDTO acRegimeRelation: acRegimeRelationList
//         ) {
//        acRegimeServiceImpl.delete(acRegimeRelation);
//    }
}

/**
*
* Method: delete(AcRegime regime)
*
*/
@Test
public void testDeleteRegime() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: update(AcRegime regimeNew)
*
*/
@Test
public void testUpdate() throws Exception {
//TODO: Test goes here...
    AcRegimeQueryCriteria acRegimeQueryCriteria = new AcRegimeQueryCriteria();
    acRegimeQueryCriteria.setEnabled(true);
    acRegimeQueryCriteria.setArId(1L);
    List<AcRegime> acRegimes = acRegimeDao.listAllByCriteria(acRegimeQueryCriteria);
    AcRegime acRegime;
    if (acRegimes != null){
        acRegime = acRegimes.get(0);
        acRegime.setRegimeCode("changeCode");
        acRegime.setRegimeName("changeCode");
        acRegime.getAcRegimeRelations().get(0).setEnabledFlag(false);
        acRegime.getAcRegimeRelations().get(1).setEnabledFlag(false);
        System.out.println(acRegime.getAcRegimeRelations().get(0));
        AcRegimeRelation acRegimeRelation = new AcRegimeRelation();
        acRegimeRelation.setId(-1L);
        acRegimeRelation.setEnabledFlag(true);
        acRegimeRelation.setRegimeaId(1L);
        acRegimeRelation.setRegimeTimeId(3L);
        acRegime.getAcRegimeRelations().add(acRegimeRelation);
        System.out.println(acRegimeDao.updateAllColumnByKey(acRegime));
        for (AcRegimeRelation ac: acRegime.getAcRegimeRelations()
             ) {
            if (ac.getId() != -1L) {// 更新
                acRegimeRelationDao.updateAllColumnByKey(ac);
            } else {
                acRegimeRelationDao.insertAllColumn(ac);
            }
        }
    }
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
* Method: listAll(AcRegimeQueryCriteria criteria)
*
*/
@Test
public void testListAllCriteria() throws Exception {
//TODO: Test goes here...
    AcRegimeQueryCriteria acRegimeQueryCriteria = new AcRegimeQueryCriteria();
    acRegimeQueryCriteria.setEnabled(true);
    acRegimeQueryCriteria.setArId(1L);
    List<AcRegimeDTO> acRegimes = acRegimeServiceImpl.listAll(acRegimeQueryCriteria);
    System.out.println(acRegimes.size());
    System.out.println(acRegimes.get(0).getAcRegimeRelations().size());
    System.out.println(acRegimes.get(0).getAcRegimeRelations().get(1).getRegimeaId());
    System.out.println(acRegimes.get(0).getAcRegimeRelations().get(1).getRegimeTimeId());
}

/**
*
* Method: listAll(AcRegimeQueryCriteria criteria, Pageable pageable)
*
*/
@Test
public void testListAllForCriteriaPageable() throws Exception {
//TODO: Test goes here...
    Sort sort = Sort.by("ar_temp.ar_id");//.by(Sort.Direction.ASC, "id");
    Pageable pageable = PageRequest.of(0, 10, sort);
    AcRegimeQueryCriteria acRegimeQueryCriteria = new AcRegimeQueryCriteria();
    acRegimeQueryCriteria.setEnabled(true);
    acRegimeQueryCriteria.setArId(1L);
    Map<String,Object> acRegimes = acRegimeServiceImpl.listAll(acRegimeQueryCriteria,pageable);
    System.out.println(acRegimes.toString());
}

/**
*
* Method: download(List<AcRegimeDTO> regimeDTOS, HttpServletResponse response)
*
*/
@Test
public void testDownload() throws Exception {
//TODO: Test goes here...

//    AcRegimeRelation acRegimeRelation = new AcRegimeRelation();
//    acRegimeRelation.setId(-1L);
//    System.out.println(acRegimeRelation.getId() == -1L);
    LocalDate now = LocalDate.now();
    LocalDate now1 = LocalDate.now();
    LocalTime nowTime = LocalTime.of(12,12);
    LocalTime nowTime1 = LocalTime.of(12,12);
    LocalTime nowTime2 = LocalTime.of(13,0);
    System.out.println(now.isAfter(now1));
    System.out.println((now.isAfter(now1) || now.equals(now1)));
    System.out.println(nowTime.isAfter(nowTime1));
    System.out.println(nowTime.isAfter(nowTime1) || nowTime.equals(nowTime1));
    System.out.println(nowTime2.isAfter(nowTime1));
}

//// 测试时间重叠
//    public Boolean checkTimeCross(List<AcRegimeRelation> acRegimeRelations) {
//        for (AcRegimeRelation arr : acRegimeRelations
//        ) {
//            AcRegimeTime art = arr.getAcRegimeTime();
//            for (AcRegimeRelation tempArr : acRegimeRelations
//            ) {
//                if (arr.getId().longValue() != tempArr.getId().longValue()) {
//                    if (art.getTimeFrom().getLong(ChronoField.MILLI_OF_SECOND) <= tempArr.getAcRegimeTime().getTimeTo().getLong(ChronoField.MILLI_OF_SECOND)
//                            && art.getTimeFrom().getLong(ChronoField.MILLI_OF_SECOND) >= tempArr.getAcRegimeTime().getTimeFrom().getLong(ChronoField.MILLI_OF_SECOND)
//                    ) {
//                        return false;
//                    }
//                    if (art.getTimeTo().getLong(ChronoField.MILLI_OF_SECOND) <= tempArr.getAcRegimeTime().getTimeTo().getLong(ChronoField.MILLI_OF_SECOND)
//                            && art.getTimeTo().getLong(ChronoField.MILLI_OF_SECOND) >= tempArr.getAcRegimeTime().getTimeFrom().getLong(ChronoField.MILLI_OF_SECOND)
//                            && !art.getExtendTimeFlag()
//                    ) { //非跨日 且在区间内
//                        return false;
//                    }
//                }
//            }
//        }
//        return true;
//    }
//
//    @Test
//    public void testCheckTime() {
//
//    }

}
