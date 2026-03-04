package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.dao.AcCalendarLineDao;
import com.sunten.hrms.ac.domain.AcCalendarLine;
import com.sunten.hrms.ac.dto.AcCalendarLineQueryCriteria;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
* AcCalendarLineServiceImpl Tester.
*
* @author <Authors name>
* @since <pre>09/22/2020</pre>
* @version 1.0
*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class AcCalendarLineServiceImplTest {
@Autowired
    AcCalendarLineDao acCalendarLineDao;
@Autowired
    AcCalendarLineService acCalendarLineService;
@Before
public void before() throws Exception {
}

@After
public void after() throws Exception {
}

/**
*
* Method: insert(AcCalendarLine calendarLineNew)
*
*/
@Test
public void testInsert() throws Exception {
//TODO: Test goes here...
}

@Test
public void testInsertCollection() throws Exception {
//TODO: Test goes here...
    List<AcCalendarLine> acCalendarLines = this.generateAcCalendarLine();
    System.out.println(acCalendarLineDao.insertCollection(acCalendarLines));
}

@Test
public void testDefault() throws  Exception {
    List<AcCalendarLine> acCalendarLines = acCalendarLineDao.listDefaultCalendarLineByDate(LocalDate.now().minusDays(7),LocalDate.now());
    for (AcCalendarLine ac: acCalendarLines
         ) {
        System.out.println(ac);
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
}

/**
*
* Method: delete(AcCalendarLine calendarLine)
*
*/
@Test
public void testDeleteCalendarLine() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: update(AcCalendarLine calendarLineNew)
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
* Method: listAll(AcCalendarLineQueryCriteria criteria)
*
*/
@Test
public void testListAllCriteria() throws Exception {
//TODO: Test goes here...
    AcCalendarLineQueryCriteria acCalendarLineQueryCriteria = new AcCalendarLineQueryCriteria();
    acCalendarLineQueryCriteria.setCalendarHeaderId(5L);
    acCalendarLineQueryCriteria.setEnabled(true);
    LocalDate now = LocalDate.now();
    acCalendarLineQueryCriteria.setBeginDate(now.withDayOfYear(1));
    acCalendarLineQueryCriteria.setEndDate(now.withDayOfYear(5));
    List<AcCalendarLine> acCalendarLines = acCalendarLineDao.listAllByCriteria(acCalendarLineQueryCriteria);
    System.out.println(acCalendarLines);
}

/**
*
* Method: listAll(AcCalendarLineQueryCriteria criteria, Pageable pageable)
*
*/
@Test
public void testListAllForCriteriaPageable() throws Exception {
    AcCalendarLineQueryCriteria acCalendarLineQueryCriteria = new AcCalendarLineQueryCriteria();
    acCalendarLineQueryCriteria.setCalendarHeaderId(5L);
    acCalendarLineQueryCriteria.setEnabled(true);
    LocalDate now = LocalDate.now();
    acCalendarLineQueryCriteria.setBeginDate(now.withDayOfYear(1));
    acCalendarLineQueryCriteria.setEndDate(now.withDayOfYear(5));
    Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
    Pageable pageable = PageRequest.of(0, 10, sort);
    Map<String, Object> acCalendarLines = acCalendarLineService.listAll(acCalendarLineQueryCriteria, pageable);
    System.out.println(acCalendarLines);

//TODO: Test goes here...
}

/**
*
* Method: download(List<AcCalendarLineDTO> calendarLineDTOS, HttpServletResponse response)
*
*/
@Test
public void testDownload() throws Exception {
//TODO: Test goes here...
}

@Test
public void testCheckYearList() throws Exception {
//TODO: Test goes here...
//    LocalDate now = LocalDate.now();
//    Integer year = now.getYear();
//    List<AcCalendarLine> acCalendarLines = acCalendarLineDao.listAllByYear(year);
//    System.out.println(acCalendarLines.size());
}

/**
*
* Method: generateAcCalendarLines()
*
*/
@Test
public void testGenerateAcCalendarLines() throws Exception {
//TODO: Test goes here...
    /**
     *  @author：liangjw
     *  @Date: 2020/9/24 14:14
     *  @Description: 测试日历生成
     *  @params: LocalDate 年
     */
    List<AcCalendarLine> acCalendarLineList = new ArrayList<>();
    LocalDate now = LocalDate.parse("2021-01-09");
    String[][] weekOfDay = {{"MONDAY", "星期一"}, {"TUESDAY", "星期二"}, {"WEDNESDAY", "星期三"}, {"THURSDAY", "星期四"}, {"FRIDAY", "星期五"}, {"SATURDAY ", "星期六"}, {"SUNDAY", "星期日"}};
    AcCalendarLine acCalendarLine;
    LocalDate line;
    for (int i = 1; i < 366; i++) {
        acCalendarLine = new AcCalendarLine();
        line = now.withDayOfYear(i);
        acCalendarLine.setNowDate(line);
        for (int j = 0; j < weekOfDay.length; j++) {
            if (weekOfDay[j][0].contains(String.valueOf(line.getDayOfWeek()))) {
                acCalendarLine.setWeek(weekOfDay[j][1]);
                if (j < 5) {
                    acCalendarLine.setDayOffFlag(false);
                } else {
                    acCalendarLine.setDayOffFlag(true);
                }
            }
        }
        acCalendarLine.setEnabledFlag(true);
        acCalendarLine.setCalendarHeaderId(1L);
        acCalendarLineList.add(acCalendarLine);
    }
    for (int i = 0; i < acCalendarLineList.size(); i++) {
        System.out.println(acCalendarLineList.get(i));
    }
}

List<AcCalendarLine> generateAcCalendarLine() throws ParseException {
    List<AcCalendarLine> acCalendarLineList = new ArrayList<>();
    // 测试日历生成
    String dBegin = "2020-09-01";
    String dEnd = "2020-09-30";
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat eeeeFormat = new SimpleDateFormat("EEEE");
    String[] weekList = {"星期一", "星期二", "星期三", "星期四", "星期五"};
    //设置开始时间
    Calendar calBegin = Calendar.getInstance();
    calBegin.setTime(dateFormat.parse(dBegin));
    //设置结束时间
    Calendar calEnd = Calendar.getInstance();
    calEnd.setTime(dateFormat.parse(dEnd));
    // 每次循环给calBegin日期加一天，直到calBegin.getTime()时间等于dEnd
    AcCalendarLine acCalendarLine;
    acCalendarLine = new AcCalendarLine();
    acCalendarLine.setNowDate(calBegin.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    acCalendarLine.setWeek(eeeeFormat.format(calBegin.getTime()));
    for (int i = 0; i < weekList.length; i++) {
        if (weekList[i].contains(eeeeFormat.format(calBegin.getTime()))) {
            acCalendarLine.setDayOffFlag(false);
        }
    }
    acCalendarLine.setEnabledFlag(true);
    acCalendarLine.setCalendarHeaderId(1L);
    acCalendarLineList.add(acCalendarLine);
    while (dateFormat.parse(dEnd).after(calBegin.getTime()))  {
        // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
        calBegin.add(Calendar.DAY_OF_MONTH, 1);
        acCalendarLine = new AcCalendarLine();
        acCalendarLine.setNowDate(calBegin.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        acCalendarLine.setWeek(eeeeFormat.format(calBegin.getTime()));
        acCalendarLine.setDayOffFlag(true);
        for (int i = 0; i < weekList.length; i++) {
            if (weekList[i].contains(eeeeFormat.format(calBegin.getTime()))) {
                acCalendarLine.setDayOffFlag(false);
            }
        }
        acCalendarLine.setEnabledFlag(true);
        acCalendarLine.setCalendarHeaderId(1L);
        acCalendarLineList.add(acCalendarLine);
    }
    return acCalendarLineList;
}


}
