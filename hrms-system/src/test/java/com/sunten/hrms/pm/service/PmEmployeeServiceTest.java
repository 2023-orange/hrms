package com.sunten.hrms.pm.service;

import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.dao.FndJobDao;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.domain.FndJob;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndJobService;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeContract;
import com.sunten.hrms.pm.domain.PmEmployeeFamily;
import com.sunten.hrms.pm.domain.PmEmployeeJob;
import com.sunten.hrms.pm.dto.*;
import com.sunten.hrms.re.service.ReRecruitmentService;
import com.sunten.hrms.td.domain.TdTrain;
import com.sunten.hrms.td.dto.TdTrainDTO;
import com.sunten.hrms.td.dto.TdTrainQueryCriteria;
import com.sunten.hrms.td.dto.TdTrainSubQueryCriteria;
import com.sunten.hrms.td.service.TdTrainService;
import com.sunten.hrms.td.service.TdTrainSubService;
import com.sunten.hrms.vo.ElTreeBaseVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @atuthor xukai
 * @date 2020/8/7 14:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PmEmployeeServiceTest {

    @Autowired
    PmEmployeeService pmEmployeeService;
    @Autowired
    FndDeptDao fndDeptDao;
    @Autowired
    FndJobDao fndJobDao;
    @Autowired
    PmEmployeeLeaveofficeService pmEmployeeLeaveofficeService;
    @Autowired
    PmEmployeeJobService pmEmployeeJobService;
    @Autowired
    PmEmployeeEducationService pmEmployeeEducationService;

    @Autowired
    PmEmployeeContractService pmEmployeeContractService;
    @Autowired
    PmEmployeeDao pmEmployeeDao;


    @Autowired
    TdTrainService tdTrainService;
    @Autowired
    TdTrainSubService tdTrainSubService;

    @Test
    public void ListAllTest(){
        PmEmployeeQueryCriteria employeeQueryCriteria = new PmEmployeeQueryCriteria();//参数
        //部门
     ;
//        employeeQueryCriteria.setDeptAllId(1L);
        //查询参数
        employeeQueryCriteria.setEnabledFlag(true);
        //employeeQueryCriteria.setColName("name");
        //employeeQueryCriteria.setSymbol("like");
        //employeeQueryCriteria.setValue("%m%");
        //Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
        //Pageable pageable = PageRequest.of(0, 10, sort);

        List<PmEmployeeDTO> employeeDTOS =  pmEmployeeService.listAll(employeeQueryCriteria);
        System.out.println("输出查询员工信息------------------长度:"+employeeDTOS.size());
        for(PmEmployeeDTO ped :employeeDTOS){
            System.out.println(ped.toString());
        }
    }

    @Test
    public void insertEmployee(){
        PmEmployee pm = new PmEmployee();
        pm.setLeaveFlag(false);
        pm.setEnabledFlag(true);

        pm.setWorkCard("工牌1");
        pm.setName("mi1");
        pm.setNameAbbreviation("Nam1");
        pm.setGender("gen1");
        pm.setIdNumber("idN1");
        pm.setBirthday(LocalDate.now());
        pm.setHeight(16D);
        pm.setWeight(162D);
        pm.setCalendar("calr1");
        pm.setNation("na1");
        pm.setMaritalStatus("mls1");
        pm.setHouseholdCharacter("hou1");
        pm.setAddress("地址");
        pm.setZipcode("zi1");
        pm.setHouseholdAddress("ho");
        pm.setHouseholdZipcode("house");
        pm.setCollectiveHouseholdFlag(true);
        pm.setCollectiveAddress("colle");
        pm.setNativePlace("nati");
        pm.setWorkAttendanceFlag(true);
        pm.setWorkshopAttendanceFlag(true);
        pm.setEmployeeType("empl");
        pm.setLeaveFlag(false);
        pm.setOccupationCategory("occu");
        pm.setOccupationType("occu");
        pm.setEmailInside("ema ");
        pm.setAdministrationCategory("ad");
        pm.setRemarks("re");
        pm.setEnabledFlag(true);
        pm.setRegisteredResidence("re");

        PmEmployeeJob pmEmployeeJob = new PmEmployeeJob();
        FndDept dept = fndDeptDao.getByKey(new Long(69));//部门
        FndJob job = fndJobDao.getByKey(new Long(6));//岗位
        pmEmployeeJob.setEmployee(pm);
        pmEmployeeJob.setJob(job);
        pmEmployeeJob.setDept(dept);
        pmEmployeeJob.setGroupId(111L);
        pmEmployeeJob.setJobMainFlag(true);
        pmEmployeeJob.setEnabledFlag(true);
        pmEmployeeJob.setRemarks("remarks");

        pm.setMainJob(pmEmployeeJob);

        //家庭情况
        Set<PmEmployeeFamily> families = new HashSet<>();
        PmEmployeeFamily family = new PmEmployeeFamily();
        family.setEnabledFlag(true);
        family.setId(-1L);
        family.setName("徐");
        family.setRelationship("本人");
        family.setTele("1314466400");
        families.add(family);
        pm.setEmployeeFamilies(families);
        System.out.println("开始插入对象----------------");
        //System.out.println("本次插入对象："+pm.toString());
        pmEmployeeService.insert(pm);
        System.out.println("插入对象完毕----------------");
    }

    @Test
    public void levelOfficeListAll(){
        PmEmployeeLeaveofficeQueryCriteria leaveofficeQueryCriteria = new PmEmployeeLeaveofficeQueryCriteria();
//        leaveofficeQueryCriteria.setColName("procedures_flag");
//        leaveofficeQueryCriteria.setSymbol("=");
//        leaveofficeQueryCriteria.setValue("true");
        leaveofficeQueryCriteria.setEnabled(true);
        leaveofficeQueryCriteria.setLeaveTimeStart(LocalDate.of(2020,9,1));
        leaveofficeQueryCriteria.setLeaveTimeEnd(LocalDate.of(2020,9,30));
        Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(0, 10, sort);
        //List<PmEmployeeLeaveofficeDTO> dts =  pmEmployeeLeaveofficeService.listAll(leaveofficeQueryCriteria);
        Map<String,Object> dts = pmEmployeeLeaveofficeService.listAll(leaveofficeQueryCriteria,pageable);
        System.out.println("开始输出离职信息表---------------------");
//        for(PmEmployeeLeaveofficeDTO pel:dts){
//            System.out.println(pel.toString());
//        }
        System.out.println(dts.toString());
    }


    @Test
    public void getGroupId(){
        Long groupId = pmEmployeeJobService.getMaxGroupId();
        System.out.println("groupId:"+groupId);
    }

    @Test
    public void getEmployeeList(){
        PmEmployeeQueryCriteria pmEmployeeQueryCriteria = new PmEmployeeQueryCriteria();
        //pmEmployeeQueryCriteria.setLeaveFlag(false);
        pmEmployeeQueryCriteria.setEnabledFlag(true);
        Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(0, 10, sort);
        //List<PmEmployeeDTO> ss = pmEmployeeService.listAll(pmEmployeeQueryCriteria);
        Map<String, Object> ss = pmEmployeeService.listAll(pmEmployeeQueryCriteria,pageable);

        //System.out.println("输出查询的人员总数量："+ss.size());
        //for(PmEmployeeDTO ped:ss){
           // System.out.println(ped.toString());
        //}
        System.out.println(ss.toString());
    }

    @Test
    public void getLikeEmployeeList(){
        PmEmployeeLikeQueryCriteria likeQueryCriteria = new PmEmployeeLikeQueryCriteria();
        likeQueryCriteria.setIdentityNmae("%11%");
        List<ElTreeBaseVo> peDtos = pmEmployeeService.getListByNameOrCard(likeQueryCriteria);
        System.out.println("开始输入员工信息表：---------------------"+ peDtos.size());

            System.out.println(peDtos);
    }

    @Test
    public void listTest(){
        List<Map<String,String>> slist = new ArrayList<>();
        Map<String,String> map1 = new HashMap<>();
        map1.put("name","xx");
        map1.put("value","xs");
        Map<String,String> map2 = new HashMap<>();
        map2.put("name","xx2");
        map2.put("value","xs2");
        slist.add(map1);
        slist.add(map2);
        List<Map<String,String>> slist2 = new ArrayList<>();

        slist.forEach( s->{
            slist2.add(s);
        });

        for(Map s:slist2){
            s.put("name","cccc");
        }
        for(Map s:slist){
            System.out.println(s);
        }
        for(Map s:slist2){
            System.out.println(s);
        }

    }

    @Test
    public void getEducationTempList(){
       List<PmEmployeeEducationDTO> edtos = pmEmployeeEducationService.listAllByCheck(37L);
       System.out.println("开始输出学历校核信息------------------------");
       if(edtos.size()>0){
           for(PmEmployeeEducationDTO peed:edtos){
               System.out.println(peed.toString());
           }
       }
    }

    @Test
    public void testTranSub(){
        TdTrainSubQueryCriteria criteria = new TdTrainSubQueryCriteria();
        criteria.setEnabled(true);
        criteria.setTrainId(1L);
        Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(0, 10, sort);

        Map<String,Object> tdlist = tdTrainSubService.listAll(criteria,pageable);
        if(tdlist != null){
            System.out.println(tdlist.toString());
        }
    }

    @Test
    public void testTran(){
        TdTrainQueryCriteria criteria = new TdTrainQueryCriteria();
        criteria.setTrainStartTime(LocalDate.of(2019,1,1));
        criteria.setTrainEndTime(LocalDate.of(2019,12,31));
        Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(0, 10, sort);

        Map<String,Object> tdlist = tdTrainService.listAll(criteria,pageable);

        if(tdlist != null){
            System.out.println(tdlist.toString());
        }
    }

    @Test
    public void TestContract(){
        PmEmployeeContractQueryCriteria contractQueryCriteria = new PmEmployeeContractQueryCriteria();
        contractQueryCriteria.setEmployeeId(2L);
        contractQueryCriteria.setEnabled(true);
        Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(0, 10, sort);
        Map<String, Object>  map = pmEmployeeContractService.listAll(contractQueryCriteria,pageable);
        System.out.println(map.toString());
    }

    @Test
    public void getEmployeeById(){
        Long employeeId = -1L;
        PmEmployee  employee = pmEmployeeDao.getByKey(employeeId, null);
        if(employee != null && employee.getEmployeeLeaveoffice() != null){

        }
        System.out.println(employee.toString());
    }

    @Test
    public void getEmployeeByIdNumber(){
        PmEmployeeQueryCriteria pmEmployeeQueryCriteria = new PmEmployeeQueryCriteria();
        //pmEmployeeQueryCriteria.setLeaveFlag(false);
        pmEmployeeQueryCriteria.setIdNumber("440681198003203639");

        List<PmEmployeeDTO> ss = pmEmployeeService.listForIdNumberOrWorkCardExist(pmEmployeeQueryCriteria);
        //Map<String, Object> ss = pmEmployeeService.listAll(pmEmployeeQueryCriteria,pageable);

        System.out.println("输出查询的人员总数量："+ss.size());
        for(PmEmployeeDTO ped:ss){
         System.out.println(ped.toString());
        }
        //System.out.println(ss.toString());
    }
}
