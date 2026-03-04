package com.sunten.hrms;

import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @atuthor tanba
 * @date 2021/8/5 7:31
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class springTest {

    @Value("${server.port}")
    private int serverPort;

    public String getUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "http://"+address.getHostAddress() +":"+this.serverPort;
    }


    @Test
    public void test(){
        System.out.println(getUrl());

        FndDynamicQueryBaseCriteria criteria = new FndDynamicQueryBaseCriteria();
        criteria.setDynamicQueryCriteriaStr("{\"queryTable\":\"\",\"field\":\"dept_name\",\"operator\":\"EXPAND\",\"value\":\"%信息%\",\"jdbcType\":\"VARCHAR\",\"specialSql\":\" exists (SELECT 1\\n                                      FROM pm_employee_job pej_t\\n                                     WHERE pej_t.employee_id = pe.ID\\n                                       AND pej_t.enabled_flag = 1\\n                                       AND pej_t.job_main_flag = 1\\n                                       AND pej_t.dept_id IN ({{dept_name}}))\",\"fieldType\":\"DEPT\"}");
    }
}
