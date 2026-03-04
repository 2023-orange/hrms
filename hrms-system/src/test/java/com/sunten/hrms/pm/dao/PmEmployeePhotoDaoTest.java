package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeePhoto;
import com.sunten.hrms.pm.service.PmEmployeePhotoService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @atuthor xukai
 * @date 2020/9/9 10:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PmEmployeePhotoDaoTest {

    @Autowired
    PmEmployeePhotoDao pmEmployeePhotoDao;

    @Test
    public void testInsertPhoto(){
        PmEmployeePhoto employeePhoto = new PmEmployeePhoto();
        employeePhoto.setRealName("cheshi");
        employeePhoto.setAvaterSize("365.KB");
        employeePhoto.setPath("D:\\hrms\\pmPhoto\\cheshi.jpg");
        pmEmployeePhotoDao.insert(employeePhoto);
    }



}
