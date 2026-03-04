package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.pm.dto.PmEmployeeLikeQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeJobService;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.vo.ElTreeBaseVo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @atuthor xukai
 * @date 2020/8/24 12:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class PmEmployeeServiceImplTest {

    @Autowired
    PmEmployeeService pmEmployeeService;



    @Test
    void getPid() {
        PmEmployeeLikeQueryCriteria likeQueryCriteria = new PmEmployeeLikeQueryCriteria();
        likeQueryCriteria.setIdentityNmae("%11%");
        List<ElTreeBaseVo> peDtos = pmEmployeeService.getListByNameOrCard(likeQueryCriteria);
        System.out.println("开始输入员工信息表：---------------------"+ peDtos.size());
        System.out.println(peDtos.toString());

    }
}
