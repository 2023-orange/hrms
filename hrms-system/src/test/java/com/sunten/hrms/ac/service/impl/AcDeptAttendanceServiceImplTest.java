package com.sunten.hrms.ac.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Json;
import com.sunten.hrms.ac.dao.AcDeptAttendanceDao;
import com.sunten.hrms.ac.dao.AcRegimeDao;
import com.sunten.hrms.ac.domain.AcDeptAttendance;
import com.sunten.hrms.ac.domain.AcRegime;
import com.sunten.hrms.ac.dto.AcDeptAttendanceDTO;
import com.sunten.hrms.ac.dto.AcDeptAttendanceQueryCriteria;
import com.sunten.hrms.ac.dto.AcRegimeQueryCriteria;
import com.sunten.hrms.ac.mapper.AcDeptAttendanceMapper;
import com.sunten.hrms.ac.service.AcDeptAttendanceService;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.dto.FndDeptDTO;
import com.sunten.hrms.fnd.dto.FndDeptQueryCriteria;
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
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
* AcDeptAttendanceServiceImpl Tester.
*
* @author <Authors name>
* @since <pre>09/24/2020</pre>
* @version 1.0
*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class AcDeptAttendanceServiceImplTest {
    @Autowired
    AcDeptAttendanceDao acDeptAttendanceDao;
    @Autowired
    AcRegimeDao acRegimeDao;
    @Autowired
    AcDeptAttendanceService acDeptAttendanceService;
//    @Autowired
//    AcDeptAttendanceMapper acDeptAttendanceMapper;
    @Autowired
    FndDeptDao fndDeptDao;
@Before
public void before() throws Exception {
}

@After
public void after() throws Exception {
}

/**
*
* Method: insert(AcDeptAttendance deptAttendanceNew)
*
*/
@Test
public void testInsert() throws Exception {
    AcDeptAttendance acDeptAttendance = new AcDeptAttendance();
    acDeptAttendance.setRegimeId(6L);
    acDeptAttendance.setCalendarHeaderId(5L);
    acDeptAttendance.setDeptId(1L);
    acDeptAttendance.setEnabledFlag(true);
    AcRegimeQueryCriteria acRegimeQueryCriteria = new AcRegimeQueryCriteria();
    acRegimeQueryCriteria.setArId(6L);
    List<AcRegime> acRegimes = acRegimeDao.listAllByCriteria(acRegimeQueryCriteria);
    if (acRegimes.size() > 0) {
        AcRegime acRegimeTarget = acRegimes.get(0);
        if (acRegimeTarget.getAcRegimeRelations().size() > 0) {
            int size = acRegimeTarget.getAcRegimeRelations().size();
            size --;
            if (size > -1) {
                acDeptAttendance.setFirstTimeFrom(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getTimeFrom());
                acDeptAttendance.setFirstTimeTo(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getTimeTo());
                acDeptAttendance.setExtend1TimeFlag(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getExtendTimeFlag());
            }
            size --;
            if (size > -1) {
                acDeptAttendance.setSecondTimeFrom(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getTimeFrom());
                acDeptAttendance.setSecondTimeTo(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getTimeTo());
                acDeptAttendance.setExtend2TimeFlag(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getExtendTimeFlag());
            }
            size --;
            if (size > -1) {
                acDeptAttendance.setThirdTimeFrom(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getTimeFrom());
                acDeptAttendance.setThirdTimeTo(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getTimeTo());
                acDeptAttendance.setExtend3TimeFlag(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getExtendTimeFlag());
            }
        }
    }
    LocalDate now = LocalDate.now();
    LocalDate effectDate = now.withDayOfYear(250);
//    LocalDate inValidDate = now.withDayOfYear(365);
    acDeptAttendance.setTakeEffectDate(effectDate);
//    acDeptAttendance.setInvalidDate(inValidDate);
    acDeptAttendance.setEnabledFlag(true);
    System.out.println(acDeptAttendanceDao.insertAllColumn(acDeptAttendance));
//TODO: Test goes here...
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
* Method: delete(AcDeptAttendance deptAttendance)
*
*/
@Test
public void testDeleteDeptAttendance() throws Exception {
//TODO: Test goes here...
}

/**
*
* Method: update(AcDeptAttendance deptAttendanceNew)
*
*/
@Test
public void testUpdate() throws Exception {
//TODO: Test goes here...
    // 获取模拟数据
    AcDeptAttendanceQueryCriteria acDeptAttendanceQueryCriteria = new AcDeptAttendanceQueryCriteria();
    acDeptAttendanceQueryCriteria.setDeptId(87L);
    acDeptAttendanceQueryCriteria.setEnabled(true);
    List<AcDeptAttendance> acDeptAttendances = acDeptAttendanceDao.baseListByCriteria(acDeptAttendanceQueryCriteria);
    // 先判断时间区间是否符合连续性
    if (acDeptAttendances.size() > 0) {
        if (null != acDeptAttendances.get(0).getInvalidDate()) {
            throw new InfoCheckWarningMessException("检测到曾经已通过变更，生成新的考勤规则。若要再次变更，请先删除上一次变更所生成的考勤规则");
        }
    }
    // 更新旧关系的失效时间
    LocalDate now = LocalDate.now();
    LocalDate validTime = now.withDayOfYear(355);
    AcDeptAttendance acDeptAttendanceOld = acDeptAttendances.get(0);
    acDeptAttendanceOld.setInvalidDate(validTime);
    acDeptAttendanceDao.updateAllColumnByKey(acDeptAttendanceOld);
    // 新增新关系
    AcDeptAttendance acDeptAttendanceRealNew = acDeptAttendances.get(0);
    acDeptAttendanceRealNew.setExtend3TimeFlag(null);
    acDeptAttendanceRealNew.setExtend2TimeFlag(null);
    acDeptAttendanceRealNew.setExtend1TimeFlag(null);
    acDeptAttendanceRealNew.setThirdTimeTo(null);
    acDeptAttendanceRealNew.setThirdTimeFrom(null);
    acDeptAttendanceRealNew.setSecondTimeFrom(null);
    acDeptAttendanceRealNew.setSecondTimeTo(null);
    acDeptAttendanceRealNew.setFirstTimeTo(null);
    acDeptAttendanceRealNew.setFirstTimeFrom(null);
    acDeptAttendanceRealNew.setId(null); // id
    acDeptAttendanceRealNew.setInvalidDate(null);
    LocalDate effectDate = validTime.plusDays(1L);
    acDeptAttendanceRealNew.setTakeEffectDate(effectDate);
    // 时间段
    acDeptAttendanceRealNew.setRegimeId(5L);
    this.setRegimeDetailTime(acDeptAttendanceRealNew);
    acDeptAttendanceDao.insertAllColumn(acDeptAttendanceRealNew);
}

public void setRegimeDetailTime(AcDeptAttendance acDeptAttendance) {
    AcRegimeQueryCriteria acRegimeQueryCriteria = new AcRegimeQueryCriteria();
    acRegimeQueryCriteria.setArId(acDeptAttendance.getRegimeId());
    List<AcRegime> acRegimes = acRegimeDao.listAllByCriteria(acRegimeQueryCriteria);
    if (acRegimes.size() > 0) {
        AcRegime acRegimeTarget = acRegimes.get(0);
        if (acRegimeTarget.getAcRegimeRelations().size() > 0) {
            int size = acRegimeTarget.getAcRegimeRelations().size();
            size --;
            if (size > -1) {
                acDeptAttendance.setFirstTimeFrom(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getTimeFrom());
                acDeptAttendance.setFirstTimeTo(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getTimeTo());
                acDeptAttendance.setExtend1TimeFlag(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getExtendTimeFlag());
            }
            size --;
            if (size > -1) {
                acDeptAttendance.setSecondTimeFrom(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getTimeFrom());
                acDeptAttendance.setSecondTimeTo(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getTimeTo());
                acDeptAttendance.setExtend2TimeFlag(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getExtendTimeFlag());
            }
            size --;
            if (size > -1) {
                acDeptAttendance.setThirdTimeFrom(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getTimeFrom());
                acDeptAttendance.setThirdTimeTo(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getTimeTo());
                acDeptAttendance.setExtend3TimeFlag(acRegimeTarget.getAcRegimeRelations().get(size).getAcRegimeTime().getExtendTimeFlag());
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
* Method: listAll(AcDeptAttendanceQueryCriteria criteria)
*
*/
@Test
public void testListAllCriteria() throws Exception {
//TODO: Test goes here...
    AcDeptAttendanceQueryCriteria acDeptAttendanceQueryCriteria = new AcDeptAttendanceQueryCriteria();
    acDeptAttendanceQueryCriteria.setRegimeId(5L);
    List<AcDeptAttendance> acDeptAttendances = acDeptAttendanceDao.listAllByCriteria(acDeptAttendanceQueryCriteria);
    for (int i = 0; i < acDeptAttendances.size(); i++) {
        System.out.println(acDeptAttendances.get(i));
//        if (acDeptAttendances.get(i).getAcRegime().getAcRegimeRelations().size() > 0) {
//            for (int j = 0; j < acDeptAttendances.get(i).getAcRegime().getAcRegimeRelations().size() ; j++) {
//                System.out.println(acDeptAttendances.get(i).getAcRegime().getAcRegimeRelations().get(j).getAcRegimeTime());
//            }
//        }
    }
}

@Test
public void testBaseCriteria() throws Exception {
    AcDeptAttendanceQueryCriteria acDeptAttendanceQueryCriteria = new AcDeptAttendanceQueryCriteria();
    acDeptAttendanceQueryCriteria.setEnabled(true);
    acDeptAttendanceQueryCriteria.setRegimeId(5L);
    acDeptAttendanceQueryCriteria.setNowAndAfter(true);
//    acDeptAttendanceQueryCriteria.setHistory(false);
    List<AcDeptAttendance> acDeptAttendances = acDeptAttendanceDao.baseListByCriteria(acDeptAttendanceQueryCriteria);
    for (int i = 0; i < acDeptAttendances.size(); i++) {
        System.out.println(acDeptAttendances.get(i));
    }
}

/**
*
* Method: listAll(AcDeptAttendanceQueryCriteria criteria, Pageable pageable)
*
*/
@Test
public void testListAllForCriteriaPageable() throws Exception {
//TODO: Test goes here...
    Sort sort = Sort.by("fd.fd_id");//.by(Sort.Direction.ASC, "id");
    Pageable pageable = PageRequest.of(0, 10, sort);
    AcDeptAttendanceQueryCriteria acDeptAttendanceQueryCriteria = new AcDeptAttendanceQueryCriteria();
    acDeptAttendanceQueryCriteria.setDeptId(87L);
    acDeptAttendanceQueryCriteria.setHistory(true);
    Map<String,Object> acDeptAttendances = acDeptAttendanceService.listAll(acDeptAttendanceQueryCriteria,pageable);
    System.out.println(acDeptAttendances.get("content"));
}

/**
*
* Method: download(List<AcDeptAttendanceDTO> deptAttendanceDTOS, HttpServletResponse response)
*
*/
@Test
public void testDownload() throws Exception {
//TODO: Test goes here...
}


@Test
public void builTree() throws Exception {
//    AcDeptAttendanceQueryCriteria acDeptAttendanceQueryCriteria = new AcDeptAttendanceQueryCriteria();
//    List<AcDeptAttendanceDTO> acDeptAttendanceDTOS = acDeptAttendanceMapper.toDto(acDeptAttendanceDao.listAllByCriteria(acDeptAttendanceQueryCriteria));
//    System.out.println(acDeptAttendanceDTOS);
//    Set<AcDeptAttendanceDTO> trees = new LinkedHashSet<>();
//    Set<AcDeptAttendanceDTO> depts = new LinkedHashSet<>();
//    List<FndDeptDTO> fndDepts = acDeptAttendanceDTOS.stream().map(AcDeptAttendanceDTO::getFndDept).collect(Collectors.toList());
//    List<String> deptNames = fndDepts.stream().map(FndDeptDTO::getDeptName).collect(Collectors.toList());
//    Boolean isChild;
//    for (AcDeptAttendanceDTO acDeptAttendanceDTO: acDeptAttendanceDTOS
//    ) {
//        isChild = false;
//        if ("0".equals(acDeptAttendanceDTO.getFndDept().getParentId().toString())) {
//            trees.add(acDeptAttendanceDTO); // 父节点直接添加
//        }
//        for (AcDeptAttendanceDTO it : acDeptAttendanceDTOS
//        ) {
//            if (it.getFndDept().getParentId().equals(acDeptAttendanceDTO.getFndDept().getId())) { // 如果为子节点添加
//                isChild = true;
//                if (acDeptAttendanceDTO.getChildren() == null) {
//                    acDeptAttendanceDTO.setChildren(new ArrayList<>());
//                }
//                acDeptAttendanceDTO.getChildren().add(it);
//            }
//        }
//        if (isChild) {
//            depts.add(acDeptAttendanceDTO);
//        } else if (!deptNames.contains(acDeptAttendanceDao.getByDeptId(acDeptAttendanceDTO.getFndDept().getParentId()).getFndDept().getDeptName())) {
//            System.out.println("dept-----");
//            System.out.println(acDeptAttendanceDao.getByDeptId(acDeptAttendanceDTO.getFndDept().getParentId()).getFndDept().getDeptName());
//            depts.add(acDeptAttendanceDTO);
//        }
//    }
//    System.out.println("depts====================");
//    System.out.println(depts);
//    if (CollectionUtils.isEmpty(trees)) {
//        trees = depts;
//    }
//    Integer totalElements = acDeptAttendanceDTOS.size();
//    Map<String, Object> map = new HashMap<>();
//    map.put("totalElements", totalElements);
//    map.put("content", CollectionUtils.isEmpty(trees) ? acDeptAttendanceDTOS : trees);
//    System.out.println(map.toString());
}

@Test
public void removeByIdAndResetOld() {
    AcDeptAttendanceQueryCriteria acDeptAttendanceQueryCriteria1 = new AcDeptAttendanceQueryCriteria();
    acDeptAttendanceQueryCriteria1.setId(5L);
    acDeptAttendanceQueryCriteria1.setHistory(true);
    acDeptAttendanceQueryCriteria1.setEnabled(true);
//    acDeptAttendanceQueryCriteria1.setId(1L);  //测试删除的前置条件成功
    List<AcDeptAttendance> acDeptAttendanceList = acDeptAttendanceDao.baseListByCriteria(acDeptAttendanceQueryCriteria1);
    if (acDeptAttendanceList.size() == 0) { // 在列表上显示的东西不见了
        throw new InfoCheckWarningMessException("当前操作的制度关系已经发生变化，请刷新");
    }
    AcDeptAttendance tar = acDeptAttendanceList.get(0);
    AcDeptAttendance deptAttendanceInDb = Optional.ofNullable(acDeptAttendanceDao.getByKey(tar.getId())).orElseGet(AcDeptAttendance::new);
    ValidationUtil.isNull(deptAttendanceInDb.getId() ,"DeptAttendance", "id", tar.getId());
    tar.setId(deptAttendanceInDb.getId());
    // 当前生效的不允许删除
    AcDeptAttendanceQueryCriteria acDeptAttendanceQueryCriteria = new AcDeptAttendanceQueryCriteria();
//    acDeptAttendanceQueryCriteria.setHistory(true); // 为true时查全部
    acDeptAttendanceQueryCriteria.setEnabled(true);
    acDeptAttendanceQueryCriteria.setDeptId(tar.getDeptId());
    List<AcDeptAttendance> acDeptAttendances = acDeptAttendanceDao.baseListByCriteria(acDeptAttendanceQueryCriteria);
    if (acDeptAttendances.size() > 0) {
        if (acDeptAttendances.get(0).getId().equals(tar.getId())) {
            throw new InfoCheckWarningMessException("不允许删除当前已经生效的制度关系");
        }
    }
    // 没抛出异常则表示删除其它未生效的,执行删除
    tar.setEnabledFlag(false);
    acDeptAttendanceDao.updateAllColumnByKey(tar);
    // 更新当前生效的规则的失效日期未null
    AcDeptAttendance acDeptAttendanceOld = acDeptAttendances.get(0);
    acDeptAttendanceOld.setInvalidDate(null);
    acDeptAttendanceDao.updateAllColumnByKey(acDeptAttendanceOld);
}


@Test
public void testDeptAttendanceQueryLoop() {
    // 按每天生成副本， 获取x日的副本
    // 假装模拟
    FndDeptQueryCriteria fndDeptQueryCriteria = new FndDeptQueryCriteria();
    fndDeptQueryCriteria.setEnabled(true);
    List<FndDept> deptTemps = fndDeptDao.listAllByCriteria(fndDeptQueryCriteria);
    // 获取副本完成
    // 获取当前所有的关系
    AcDeptAttendanceQueryCriteria acDeptAttendanceQueryCriteria = new AcDeptAttendanceQueryCriteria();
    acDeptAttendanceQueryCriteria.setEnabled(true);
    acDeptAttendanceQueryCriteria.setDate(LocalDate.now());
    List<AcDeptAttendance> acDeptAttendances = acDeptAttendanceDao.baseListByCriteria(acDeptAttendanceQueryCriteria);
//    List<AcDeptAttendance> acDeptAttendanceFulls = new ArrayList<>();
    Set<AcDeptAttendance> acDeptAttendanceFulls = new HashSet<>();
    AcDeptAttendance max = new AcDeptAttendance();
    for (AcDeptAttendance ac: acDeptAttendances
         ) {
        System.out.println(ac);
        if (ac.getDeptId() == 1L) {
            max = ac;
            System.out.println(max);
        }
    }
    // 获取x日的deptAttendance情况e
    for (FndDept dept : deptTemps
         ) {
        Boolean checkFlag = false;
        AcDeptAttendance acDeptAttendance = new AcDeptAttendance();
        for (AcDeptAttendance acDeptAttendace : acDeptAttendances
             ) {
            if (acDeptAttendace.getDeptId().longValue() == dept.getId().longValue()) {
                checkFlag = true;
                System.out.println("检查标记true");
                // 赋值
                acDeptAttendanceFulls.add(acDeptAttendace);
                break;
            }
        }
        if (!checkFlag) { // 没匹对上，调用递归找父节点
            System.out.println("没找到对应的部门关系。开始递归");
            System.out.println("输出当前dept-=-=-=-===-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            System.out.println(dept);
            JSONObject.toJSON(acDeptAttendances);
            acDeptAttendance = JSONObject.parseObject(JSONObject.toJSONString(AcDeptAttendanceLoop(dept, acDeptAttendances, max)), AcDeptAttendance.class);
            acDeptAttendance.setDeptId(dept.getId());
            acDeptAttendanceFulls.add(acDeptAttendance);
        }
    }
    for (AcDeptAttendance ac: acDeptAttendanceFulls
    ) {
        System.out.println(ac);
    }
}

// 递归部门， 部门排班关系，最高节点的排班关系
public AcDeptAttendance AcDeptAttendanceLoop (FndDept fndDept, List<AcDeptAttendance> acDeptAttendances, AcDeptAttendance max) {
    if (fndDept.getParentId() != 0L) { // 不为最高节点时，将父节点与排班关系比对
        for (AcDeptAttendance acDeptAttendance : acDeptAttendances
        ) {
            if (fndDept.getParentId().longValue() == acDeptAttendance.getDeptId().longValue()) {
                System.out.println("==========================父节点ID匹对上了，返回匹对上的制度==========================");
                return acDeptAttendance;
            }
        }
        // 父节点id没有匹对上，则根据pid获取父节点的fndDept对象，并递归
        FndDept parentDept = fndDeptDao.getByKey(fndDept.getParentId());
        return AcDeptAttendanceLoop(parentDept, acDeptAttendances, max);
    } else { // 父节点id为0L，直接返回max
        return max;
    }
}

@Test
public void testSort() {
    List<LocalDate> localDates = new LinkedList<>();
    LocalDate now = LocalDate.now();
    localDates.add(now);
    LocalDate date1 = LocalDate.now().plusDays(5);
    localDates.add(date1);
    LocalDate date2 = LocalDate.now().minusDays(3L);
    localDates.add(date2);
    localDates = localDates.stream().sorted(Comparator.comparing(LocalDate::getDayOfYear)).collect(Collectors.toList());
    for (LocalDate local : localDates
         ) {
        System.out.println(local);
    }
//    Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
//    Long milliSecond1 = LocalDateTime.now().plusHours(2L).toInstant(ZoneOffset.of("+8")).toEpochMilli();
//    Long milliSecond2 = LocalDateTime.now().plusHours(3L).toInstant(ZoneOffset.of("+8")).toEpochMilli();
//    Long milliSecond3 = LocalDateTime.now().plusHours(5L).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    LocalTime milliSecond = LocalTime.now();
    LocalTime milliSecond1 = LocalTime.now().plusHours(2L);
    LocalTime milliSecond2 = LocalTime.now().plusHours(4L);
    LocalTime milliSecond3 = LocalTime.now().plusHours(8L);
    System.out.println(milliSecond);
    System.out.println(milliSecond1);
    System.out.println(milliSecond2);
    System.out.println(milliSecond3);
}

}
