package com.sunten.hrms.wta.config;

import com.sunten.hrms.config.GlobalVariablesSingleton;
import com.sunten.hrms.fnd.dao.FndServerInformationDao;
import com.sunten.hrms.fnd.domain.FndServerInformation;
import com.sunten.hrms.fnd.dto.FndServerInformationQueryCriteria;
import com.sunten.hrms.wta.dao.WtaQuartzJobDao;
import com.sunten.hrms.wta.domain.WtaQuartzJob;
import com.sunten.hrms.wta.utils.QuartzManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author batan
 * @since 2019-01-07
 */
@Slf4j
@Component
@Order(value = 1)
public class JobRunner implements ApplicationRunner {

    private final WtaQuartzJobDao wtaQuartzJobDao;
    private final FndServerInformationDao fndServerInformationDao;
    private final QuartzManage quartzManage;

    @Value("${spring.datasource.druid.hrms.url}")
    private String hrmsDBUrl;

    @Value("${server.port}")
    private Long serverPort;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public JobRunner(WtaQuartzJobDao wtaQuartzJobDao, FndServerInformationDao fndServerInformationDao, QuartzManage quartzManage) {
        this.wtaQuartzJobDao = wtaQuartzJobDao;
        this.fndServerInformationDao = fndServerInformationDao;
        this.quartzManage = quartzManage;
    }

    /**
     * 项目启动时重新激活启用的定时任务
     * @param applicationArguments /
     */
    @Override
    public void run(ApplicationArguments applicationArguments){
        System.out.println("=====  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  开始定时任务注入检查  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  =====");
        FndServerInformationQueryCriteria criteria = new FndServerInformationQueryCriteria();
        criteria.setDatabaseUri(hrmsDBUrl);
        criteria.setEnabled(true);
        List<FndServerInformation> serverS = fndServerInformationDao.listAllByCriteria(criteria);
        boolean canAddJob = false;
        if (serverS == null || serverS.size() == 0) {
            canAddJob = true;
            log.info("");
            log.info("");
            log.info("       √ √ √   ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
            log.info("       OK   →                                       ←");
            log.info("       OK   →  当前数据库不在监控范围，可注入定时任务 ←");
            log.info("       OK   →                                       ←");
            log.info("       √ √ √   ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
            log.info("");
            log.info("");
            GlobalVariablesSingleton.getInstance().setServerCheckStatus("pass");
            GlobalVariablesSingleton.getInstance().setServerCheckMessage("当前数据库不在监控范围，可注入定时任务");
        } else {
            log.info("当前数据库地址：" + hrmsDBUrl);
            log.info("允许以下后端地址启动时注入定时任务：");
            for(FndServerInformation server: serverS){
                log.info(server.getBackendUri());
            }
            log.info("");
            log.info("当前后端服务地址：");
            log.info(getUrl());
            log.info("");
            for(FndServerInformation server: serverS){
                if(server.getBackendUri() != null && getUrl().startsWith(server.getBackendUri())) {
                    canAddJob = true;
                    log.info("");
                    log.info("");
                    log.info("       √ √ √   ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
                    log.info("       OK   →                                                ←");
                    log.info("       OK   →  后端服务地址在数据库的匹配列表中，可注入定时任务 ←");
                    log.info("       OK   →                                                ←");
                    log.info("       √ √ √   ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
                    log.info("");
                    log.info("");
                    GlobalVariablesSingleton.getInstance().setServerCheckStatus("pass");
                    GlobalVariablesSingleton.getInstance().setServerCheckMessage("后端服务地址在数据库的匹配列表中，可注入定时任务");
                    break;
                }
            }
            if(!canAddJob){
                log.error("");
                log.error("");
                log.error("      × × ×    ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
                log.error("      ERROR →                                                           ←");
                log.error("      ERROR →  后端服务地址不在数据库的匹配列表中，不注入定时任务！！！！！ ←");
                log.error("      ERROR →                                                           ←");
                log.error("      × × ×    ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
                log.error("");
                log.error("");
                GlobalVariablesSingleton.getInstance().setServerCheckStatus("error");
                GlobalVariablesSingleton.getInstance().setServerCheckMessage("后端服务地址不在数据库的匹配列表中，不注入定时任务！！！！！");
            }
        }
        System.out.println("=====  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑  定时任务注入检查完成  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑  =====");
        if (canAddJob) {
            System.out.println("=====  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  开始注入定时任务  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  =====");
            List<WtaQuartzJob> quartzJobs = wtaQuartzJobDao.listEnableJob();
            quartzJobs.forEach(quartzManage::addJob);
            System.out.println("=====  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑  定时任务注入完成  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑  =====");
        }
    }


    private String getUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "http://" + address.getHostAddress() + ":" + this.serverPort + this.contextPath;
    }
}
