package com.sunten.hrms.tool.service.impl;

import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ToolEmailInterfaceServiceImplTest {
    @Autowired
    ToolEmailInterfaceService toolEmailInterfaceService;

    @Test
    public void sendEmail() {
        toolEmailInterfaceService.sendEmail();
    }

    @Test
    public void sendEmailImmediate(){
        toolEmailInterfaceService.sendEmailImmediate(-1L);
    }
}
