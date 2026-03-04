package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.dao.AcSetUpDao;
import com.sunten.hrms.ac.domain.AcSetUp;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.wta.dao.WtaQuartzJobDao;
import com.sunten.hrms.wta.domain.WtaQuartzJob;
import com.sunten.hrms.wta.dto.WtaQuartzJobQueryCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class AcSetUpServiceImplTest {
    @Autowired
    AcSetUpDao acSetUpDao;

    @Autowired
    WtaQuartzJobDao wtaQuartzJobDao;

    @Test
    @Transactional(rollbackFor = Exception.class)
    // 测试事务回滚
    public void testSetUp() {
        try {
            AcSetUp acSetUp = new AcSetUp();
//            acSetUp.setStageFlag(false);
            acSetUp.setRunTime(LocalTime.now());
            acSetUp.setInterval(6);
            acSetUpDao.insertAllColumn(acSetUp);
        } catch(Exception e) {
            AcSetUp acSetUp = new AcSetUp();
            acSetUp.setStageFlag(false);
            acSetUp.setRunTime(LocalTime.now());
            acSetUp.setInterval(6);
            acSetUpDao.insertAllColumn(acSetUp);
        }
    }
    @Test
    public void testUpdate(){
        // 新增一条set_up
        AcSetUp acSetUp = new AcSetUp();
        acSetUp.setStageFlag(false);
        acSetUp.setRunTime(LocalTime.of(0,0,0));
        acSetUp.setInterval(10);
        acSetUpDao.insertAllColumn(acSetUp);

        // 获取定时任务的cron
        WtaQuartzJobQueryCriteria wtaQuartzJobQueryCriteria = new WtaQuartzJobQueryCriteria();
        wtaQuartzJobQueryCriteria.setJobName("考勤异常生成");
        List<WtaQuartzJob> wtaQuartzJobs = wtaQuartzJobDao.listAllByCriteria(wtaQuartzJobQueryCriteria);
        WtaQuartzJob wtaQuartzJob = new WtaQuartzJob();
        if (wtaQuartzJobs.size() > 0) {
            wtaQuartzJob = wtaQuartzJobs.get(0);
        }        // 更新定时任务的cron
        if (null != wtaQuartzJob) {// 0 0 0 1/10 * ?   从1号开始每10日0点更新一次
            String cron = acSetUp.getRunTime().getSecond() + " " +
                    acSetUp.getRunTime().getMinute() + " " +
                    acSetUp.getRunTime().getHour() + " " + "1/" +
                    acSetUp.getInterval().toString() + " * ?";
            wtaQuartzJob.setCronExpression(cron);
            wtaQuartzJobDao.updateAllColumnByKey(wtaQuartzJob);
            System.out.println("cron-=-=-=-=-=-=-=-==--=-=-=-=-=-=-=-" + cron);
        } else {
            throw new InfoCheckWarningMessException("没找到考勤异常的定时任务");
        }
    }

}
