package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReRecruitment;
import com.sunten.hrms.re.domain.ReVocational;
import com.sunten.hrms.re.dto.ReRecruitmentDTO;
import com.sunten.hrms.re.dto.ReRecruitmentQueryCriteria;
import com.sunten.hrms.re.dto.ReVocationalDTO;
import com.sunten.hrms.re.dto.ReVocationalQueryCriteria;
import com.sunten.hrms.utils.SpringContextHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @atuthor xukai
 * @date 2020/8/28 16:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class reAllServiceTest {
    @Autowired
    ReRecruitmentService reRecruitmentService;
    @Autowired
    ReVocationalService reVocationalService;


    @Test
    public void testreRecruitmentService(){
        //
        Sort sort = Sort.by("id");//.by(Sort.Direction.ASC, "id");
        ReRecruitmentQueryCriteria scr = new ReRecruitmentQueryCriteria();
        scr.setEnabled(true);
        Pageable pageable = PageRequest.of(0, 10, sort);
        Map<String,Object> map = reRecruitmentService.listAll(scr,pageable);
        //List<ReRecruitmentDTO> dtos = reRecruitmentService.listAll(null);
        System.out.println("----------------print list for recruitment--------------------------size:");
//        for(ReRecruitmentDTO rr :dtos){
//            System.out.println(rr.toString());
//        }
        if(map != null){
            System.out.println(map.toString());
        }
    }

    @Test
    public void testInsert(){
        ReRecruitment reRecruitment = new ReRecruitment();
        //主信息
        reRecruitment.setId(-1L);
        reRecruitment.setEnabledFlag(true);
        reRecruitment.setAddress("地址");
        reRecruitment.setBirthday(LocalDate.now());
        reRecruitment.setDeptName("信息部");
        reRecruitment.setEmail("163646@163.com");
        reRecruitment.setEntryTime(LocalDate.now());
        reRecruitment.setExpectedSalary(3.15);
        reRecruitment.setJobName("岗位名称");
        reRecruitment.setGender("男");
        reRecruitment.setName("徐徐徐");
        reRecruitment.setIdNumber("45619486981533215");
        reRecruitment.setHeight(165.3);
        reRecruitment.setWeight(165.3);
        reRecruitment.setNation("汉族");
        reRecruitment.setMaritalStatus("未婚");
        reRecruitment.setAddress("现住佛山");
        reRecruitment.setNativePlace("广西");
        reRecruitment.setOriginalContractFlag(true);
        reRecruitment.setOccupationalDiseasesFlag(false);
        reRecruitment.setConfidentialityRestrictionsFlag(true);
        reRecruitment.setHealthFlag(true);
        reRecruitment.setRelationshipFlag(false);
        reRecruitment.setRecruitmentFlag(true);
        //其它子集信息



        reRecruitmentService.insert(reRecruitment);
    }

    @Test
    public void testGetVocational(){
        ReVocationalQueryCriteria criteria = new ReVocationalQueryCriteria();
        criteria.setEnabled(true);
        criteria.setReId(2L);
        List<ReVocationalDTO> vocationals =  reVocationalService.listAll(criteria);

        for(ReVocationalDTO rv:vocationals){
            System.out.println(rv.toString());
        }
    }

    @Test
    public void test(){
        //获取全部bean组件
        String[] beanDefinitionNames = SpringContextHolder.getBeanDefinitionNames();
        for(String bean:beanDefinitionNames){
            System.out.println(bean);
        }

    }
}
