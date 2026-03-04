package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.dao.AcEmployeeAttendanceDao;
import com.sunten.hrms.ac.domain.AcCalendarLine;
import com.sunten.hrms.ac.domain.AcEmployeeAttendance;
import com.sunten.hrms.ac.domain.AcRegime;
import com.sunten.hrms.ac.dto.AcEmployeeAttendanceDTO;
import com.sunten.hrms.ac.dto.AcEmployeeAttendanceQueryCriteria;
import com.sunten.hrms.ac.service.AcEmployeeAttendanceService;
import com.sunten.hrms.ac.vo.AcEmployeeAttendanceVo;
import com.sunten.hrms.ac.vo.DateVo;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndUserDao;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.pm.service.impl.PmEmployeeServiceImpl;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
* AcEmployeeAttendanceServiceImpl Tester.
*
* @author <Authors name>
* @since <pre>09/27/2020</pre>
* @version 1.0
*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class AcEmployeeAttendanceServiceImplTest {
@Autowired
AcEmployeeAttendanceDao acEmployeeAttendanceDao;
@Autowired
PmEmployeeServiceImpl pmEmployeeServiveImpl;
@Autowired
    AcEmployeeAttendanceService acEmployeeAttendanceService;
@Autowired
    PmEmployeeDao pmEmployeeDao;

@Autowired
    FndUserDao fndUserDao;

@Before
public void before() throws Exception {
}

@After
public void after() throws Exception {
}

/**
*
* Method: insert(AcEmployeeAttendance employeeAttendanceNew)
*
*/
@Test
public void testInsert() throws Exception {
//TODO: Test goes here...

    //50(Long), 84(Long), 88(Long), 39(Long), 97(Long)
    // 226 227 228 223 224
    LocalDate now = LocalDate.now();
    AcEmployeeAttendance acEmployeeAttendance = new AcEmployeeAttendance();
    acEmployeeAttendance.setEmployeeId(54L);
    acEmployeeAttendance.setRegimeaDate(now.plusDays(3L));
    acEmployeeAttendance.setExtend1TimeFlag(false);
    LocalTime timeStart1 = LocalTime.of(9,15);
    acEmployeeAttendance.setFirstTimeFrom(timeStart1);
    LocalTime timeEnd1 = LocalTime.of(13,0);
    acEmployeeAttendance.setFirstTimeTo(timeEnd1);
    LocalTime timeStart2 =  LocalTime.of(14,15);
    acEmployeeAttendance.setSecondTimeFrom(timeStart2);
    LocalTime timeEnd2 = LocalTime.of(20,0);
    acEmployeeAttendance.setSecondTimeTo(timeEnd2);
    acEmployeeAttendance.setExtend2TimeFlag(false);
    acEmployeeAttendance.setEnabledFlag(true);
    acEmployeeAttendance.setRestDayFlag(false);
    if (acEmployeeAttendance.getRestDayFlag()) {
        this.setAcEmployeeAttendanceNull(acEmployeeAttendance);
    } else {
        if (this.validTime(acEmployeeAttendance)) {
            throw new InfoCheckWarningMessException("时间段不允许重叠");
        }
        if (!this.validAfterToday(acEmployeeAttendance)) {
            throw new InfoCheckWarningMessException("不允许新增今天之前的排班");
        }
    }
    acEmployeeAttendanceDao.insertAllColumn(acEmployeeAttendance);
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
* Method: delete(AcEmployeeAttendance employeeAttendance)
*
*/
@Test
public void testDeleteEmployeeAttendance() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: update(AcEmployeeAttendance employeeAttendanceNew)
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
* Method: listAll(AcEmployeeAttendanceQueryCriteria criteria)
*
*/
@Test
public void testListAllCriteria() throws Exception {
//TODO: Test goes here...
    PmEmployeeQueryCriteria pmEmployeeQueryCriteria = new PmEmployeeQueryCriteria();
    pmEmployeeQueryCriteria.setDeptAllId(85L);
//    pmEmployeeServiveImpl.getEmployeesJobList(pmEmployeeQueryCriteria);
    System.out.println(pmEmployeeQueryCriteria.getEmployeeJObs());

    AcEmployeeAttendanceQueryCriteria acEmployeeAttendanceQueryCriteria = new AcEmployeeAttendanceQueryCriteria();
//    acEmployeeAttendanceQueryCriteria.setMonth(LocalDate.now());
    acEmployeeAttendanceQueryCriteria.setEmployeeJObs(pmEmployeeQueryCriteria.getEmployeeJObs());
//    acEmployeeAttendanceQueryCriteria.setDateFrom(LocalDate.now().withDayOfMonth(1));
//    acEmployeeAttendanceQueryCriteria.setDateTo(LocalDate.now().withDayOfMonth(30));
    List<AcEmployeeAttendanceDTO> acEmployeeAttendanceDTOS = acEmployeeAttendanceService.listAll(acEmployeeAttendanceQueryCriteria);
//    System.out.println(acEmployeeAttendanceDTOS);
    // 组成合适的对象再返回
    Set<Long> empIdList = new HashSet<>();
//    for (AcEmployeeAttendanceDTO ac : acEmployeeAttendanceDTOS
//         ) {
//        empIdList.add(ac.getEmployeeId());
//    }
//    List<AcEmployeeAttendanceDTO> completeEntitys = new ArrayList<>();




    List<AcEmployeeAttendanceDTO> target = new ArrayList<>();
    for (Long empId: empIdList
         ) {
        AcEmployeeAttendanceDTO acEmployeeAttendanceDTO = new AcEmployeeAttendanceDTO();
        List<AcEmployeeAttendanceDTO> empAttendanceList = new ArrayList<>();
        for (AcEmployeeAttendanceDTO ac : acEmployeeAttendanceDTOS
             ) {
            if (ac.getEmployeeId().longValue() == empId.longValue()) {
                empAttendanceList.add(ac);
            }
        }
    }
}

/**
*
* Method: listAll(AcEmployeeAttendanceQueryCriteria criteria, Pageable pageable)
*
*/
@Test
public void testListAllForCriteriaPageable() throws Exception {
//TODO: Test goes here...
    PmEmployeeQueryCriteria pmEmployeeQueryCriteria = new PmEmployeeQueryCriteria();
    pmEmployeeQueryCriteria.setDeptAllId(85L);
//    pmEmployeeServiveImpl.getEmployeesJobList(pmEmployeeQueryCriteria);
    AcEmployeeAttendanceQueryCriteria acEmployeeAttendanceQueryCriteria = new AcEmployeeAttendanceQueryCriteria();
    acEmployeeAttendanceQueryCriteria.setMonth(LocalDate.now());
    acEmployeeAttendanceQueryCriteria.setEmployeeJObs(pmEmployeeQueryCriteria.getEmployeeJObs());
    Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
    Pageable pageable = PageRequest.of(0, 10, sort);
    Map<String,Object> object = acEmployeeAttendanceService.listAll(acEmployeeAttendanceQueryCriteria, pageable);
    System.out.println(object.toString());
}

/**
*
* Method: download(List<AcEmployeeAttendanceDTO> employeeAttendanceDTOS, HttpServletResponse response)
*
*/
@Test
public void testDownload() throws Exception {
//TODO: Test goes here...
}

@Test
public void testListLineAndAllByCriteria() throws  Exception {
    AcEmployeeAttendanceQueryCriteria acEmployeeAttendanceQueryCriteria = new AcEmployeeAttendanceQueryCriteria();
    acEmployeeAttendanceQueryCriteria.setDateFrom(LocalDate.now().minusDays(5L));
    acEmployeeAttendanceQueryCriteria.setDateTo(LocalDate.now());
    acEmployeeAttendanceQueryCriteria.setEnabled(true);
    List<AcCalendarLine> acLines = acEmployeeAttendanceDao.listLineAndAllByCriteria(acEmployeeAttendanceQueryCriteria);
    for (AcCalendarLine acl: acLines
         ) {
        System.out.println(acl.getNowDate());
        for (AcEmployeeAttendance ac: acl.getAcEmployeeAttendances()
             ) {
            System.out.println(ac);
        }
    }
//    PmEmployeeQueryCriteria pmEmployeeQueryCriteria = new PmEmployeeQueryCriteria();
//    pmEmployeeQueryCriteria.setDeptAllId(85L);
//    pmEmployeeServiveImpl.getEmployeesJobList(pmEmployeeQueryCriteria);
//    AcEmployeeAttendanceQueryCriteria acEmployeeAttendanceQueryCriteria = new AcEmployeeAttendanceQueryCriteria();
////    acEmployeeAttendanceQueryCriteria.setMonth(LocalDate.now().plusDays(20));
//    acEmployeeAttendanceQueryCriteria.setEmployeeJObs(pmEmployeeQueryCriteria.getEmployeeJObs());
//    System.out.println(pmEmployeeQueryCriteria.getEmployeeJObs());
//
//    List<Long> idList = new ArrayList<>();
//    for (int i = 0; i < acEmployeeAttendanceQueryCriteria.getEmployeeJObs().size(); i++) {
//        idList.add(acEmployeeAttendanceQueryCriteria.getEmployeeJObs().get(i).getEmployee().getId());
//    }
//    // 查出人员集合
//    List<PmEmployee> pmEmployeeList = pmEmployeeDao.selectByEmpIdList(idList);
//    System.out.println(idList);
//    for (int i = 0; i < pmEmployeeList.size(); i++) {
//        System.out.println(pmEmployeeList.get(i));
//    }
////    acEmployeeAttendanceQueryCriteria.setEmployeeId();
////    acEmployeeAttendanceQueryCriteria.setDateTo(LocalDate.now().plusDays(3));
////    acEmployeeAttendanceQueryCriteria.setDateFrom(LocalDate.now().plusDays(4));
//    // 根据日历查出排班
//    List<AcCalendarLine> acCalendarLines = acEmployeeAttendanceDao.listLineAndAllByCriteria(acEmployeeAttendanceQueryCriteria);
//
//    // 开始组建Vo
//    List<DateVo> dateVos = new ArrayList<>();
//    for (AcCalendarLine ac : acCalendarLines
//         ) {
//        DateVo dateVo = new DateVo();
//        dateVo.setDate(ac.getNowDate());
//        List<AcEmployeeAttendanceVo> acEmployeeAttendanceVos = new ArrayList<>();
//        if (null == ac.getAcEmployeeAttendances()) {
//            // 没有关系,即没有人员排班，组建人员
//            for (PmEmployee pmEmployee : pmEmployeeList
//                 ) {
//                AcEmployeeAttendanceVo acEmployeeAttendanceVo = new AcEmployeeAttendanceVo();
//                acEmployeeAttendanceVo.setId(-1L); // 补齐人员默认-1
//                acEmployeeAttendanceVo.setEmployeeId(pmEmployee.getId());
//                acEmployeeAttendanceVo.setEmployeeName(pmEmployee.getName());
//                acEmployeeAttendanceVos.add(acEmployeeAttendanceVo);
//            }
//            dateVo.setAcEmployeeAttendances(acEmployeeAttendanceVos);
//        } else { // 该天存在关系
//            // 取差集
////            List<String> names = ac.getAcEmployeeAttendances().stream().map(p -> p.getEmployee().getName()).collect(Collectors.toList());
////            List<PmEmployee> reduceEmpList = pmEmployeeList.stream().filter(item -> !ac.getAcEmployeeAttendances().stream().map(p -> p.getEmployee().getName()).collect(Collectors.toList()).contains(item.getName())).collect(Collectors.toList());
////            System.out.println("没有排班的员工");
////            System.out.println(reduceEmpList);
////            for (PmEmployee pmEmployee : reduceEmpList
////                 ) {
////
////            }
//            // 由于需要按顺序组，先当无排班进行插入
//            for (PmEmployee pmEmployee : pmEmployeeList
//            ) {
//                AcEmployeeAttendanceVo acEmployeeAttendanceVo = new AcEmployeeAttendanceVo();
//                acEmployeeAttendanceVo.setId(-1L); // 补齐人员默认-1
//                acEmployeeAttendanceVo.setEmployeeId(pmEmployee.getId());
//                acEmployeeAttendanceVo.setEmployeeName(pmEmployee.getName());
//                for (AcEmployeeAttendance acEmployeeAttendanceTemp: ac.getAcEmployeeAttendances()
//                     ) {
//                    if (acEmployeeAttendanceTemp.getEmployee().getName().equals(acEmployeeAttendanceVo.getEmployeeName())) {
//                        // 等于的直接赋值VO
//                        acEmployeeAttendanceVo.setId(acEmployeeAttendanceTemp.getId());
//                        acEmployeeAttendanceVo.setAttribute1(acEmployeeAttendanceTemp.getAttribute1());
//                        acEmployeeAttendanceVo.setAttribute2(acEmployeeAttendanceTemp.getAttribute2());
//                        acEmployeeAttendanceVo.setAttribute3(acEmployeeAttendanceTemp.getAttribute3());
//                        acEmployeeAttendanceVo.setEnabledFlag(acEmployeeAttendanceTemp.getEnabledFlag());
//                        acEmployeeAttendanceVo.setExtend1TimeFlag(acEmployeeAttendanceTemp.getExtend1TimeFlag());
//                        acEmployeeAttendanceVo.setExtend2TimeFlag(acEmployeeAttendanceTemp.getExtend2TimeFlag());
//                        acEmployeeAttendanceVo.setExtend3TimeFlag(acEmployeeAttendanceTemp.getExtend3TimeFlag());
//                        acEmployeeAttendanceVo.setFirstTimeFrom(acEmployeeAttendanceTemp.getFirstTimeFrom());
//                        acEmployeeAttendanceVo.setFirstTimeTo(acEmployeeAttendanceTemp.getFirstTimeTo());
//                        acEmployeeAttendanceVo.setSecondTimeFrom(acEmployeeAttendanceTemp.getSecondTimeFrom());
//                        acEmployeeAttendanceVo.setSecondTimeTo(acEmployeeAttendanceTemp.getSecondTimeTo());
//                        acEmployeeAttendanceVo.setThirdTimeFrom(acEmployeeAttendanceTemp.getThirdTimeFrom());
//                        acEmployeeAttendanceVo.setThirdTimeTo(acEmployeeAttendanceTemp.getThirdTimeTo());
//                        acEmployeeAttendanceVo.setRestDayFlag(acEmployeeAttendanceTemp.getRestDayFlag());
//                    }
//                }
//                acEmployeeAttendanceVos.add(acEmployeeAttendanceVo);
//            }
//            dateVo.setAcEmployeeAttendances(acEmployeeAttendanceVos);
//        }
//        dateVos.add(dateVo);
//    }
//    for (int i = 0; i < dateVos.size(); i++) {
//        System.out.println(dateVos.get(i));
//    }
}

    @Test
    public void getByUserName() {
//        System.out.println(fndUserDao.getByUsername("peixun"));
        FndUser fndUser = fndUserDao.getByUsername("peixun");
        System.out.println(fndUser);
    }

@Test
    public void remove() {
    List<AcRegime> acRegimes = new ArrayList<>();
    AcRegime acRegime = new AcRegime();
    acRegime.setId(1L);
    acRegime.setRegimeName("111");
    AcRegime acRegime1 = new AcRegime();
    acRegime1.setId(2L);
    acRegime1.setRegimeName("111");
    AcRegime acRegime2 = new AcRegime();
    acRegime2.setId(3L);
    acRegime2.setRegimeName("222");
    acRegimes.add(acRegime);
    acRegimes.add(acRegime1);
    acRegimes.add(acRegime2);
    Set<AcRegime> acs = acRegimes.stream().collect(Collectors.toCollection(() -> new TreeSet<AcRegime>(Comparator.comparing(AcRegime :: getRegimeName))));
    System.out.println(acs);
}

    void setAcEmployeeAttendanceNull(AcEmployeeAttendance acEmployeeAttendance) {
        acEmployeeAttendance.setExtend2TimeFlag(null);
        acEmployeeAttendance.setSecondTimeTo(null);
        acEmployeeAttendance.setSecondTimeFrom(null);
        acEmployeeAttendance.setFirstTimeTo(null);
        acEmployeeAttendance.setFirstTimeFrom(null);
        acEmployeeAttendance.setExtend1TimeFlag(null);
        acEmployeeAttendance.setExtend3TimeFlag(null);
        acEmployeeAttendance.setThirdTimeFrom(null);
        acEmployeeAttendance.setThirdTimeTo(null);
    }

    Boolean validTime(AcEmployeeAttendance acEmployeeAttendance) {
        Boolean falseFlag = false;
        if (null != acEmployeeAttendance.getExtend1TimeFlag() && null != acEmployeeAttendance.getExtend2TimeFlag()) {
            if (acEmployeeAttendance.getFirstTimeTo().isAfter(acEmployeeAttendance.getSecondTimeFrom())) {
                falseFlag = true;
            }
        }
        if (null != acEmployeeAttendance.getExtend2TimeFlag() && null != acEmployeeAttendance.getExtend3TimeFlag()) {
            if (acEmployeeAttendance.getSecondTimeTo().isAfter(acEmployeeAttendance.getThirdTimeFrom())) {
                falseFlag = true;
            }
        }
        return falseFlag;
    }

    Boolean validAfterToday(AcEmployeeAttendance acEmployeeAttendance) {
        Boolean validAfterTodayFlag = true;
        LocalDate now = LocalDate.now();
        if (acEmployeeAttendance.getRegimeaDate().isBefore(now)) {
            validAfterTodayFlag = false;
        }
        return validAfterTodayFlag;
    }


}
