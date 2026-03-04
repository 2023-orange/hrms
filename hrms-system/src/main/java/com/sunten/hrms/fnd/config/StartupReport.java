package com.sunten.hrms.fnd.config;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.sunten.hrms.config.GlobalVariablesSingleton;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.domain.ToolEmailServer;
import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.wta.dao.WtaQuartzJobDao;
import com.sunten.hrms.wta.domain.WtaQuartzJob;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author batan
 * @since 2022-12-05
 */
@Component
@Order(value = 2)
public class StartupReport implements ApplicationRunner {
    private final ToolEmailInterfaceService toolEmailInterfaceService;
    private final RedisTemplate redisTemplate;
    private final WtaQuartzJobDao wtaQuartzJobDao;

    @Resource(name = "scheduler")
    private Scheduler scheduler;

    @Value("${sunten.in-sunten-email-server-id}")
    private Long inSuntenEmailServerId;

    @Value("${spring.profiles.active}")
    private String profilesActive;

    @Value("${sunten.startup-report.send-profiles}")
    private String startupReportSendProfiles;

    @Value("${sunten.startup-report.send-to}")
    private String startupReportSendTo;

    @Value("${spring.datasource.druid.hrms.url}")
    private String hrmsDBUrl;

    @Value("${spring.datasource.druid.hrms.user}")
    private String hrmsDBUser;

    @Value("${spring.datasource.druid.oa.xaProperties.url}")
    private String oaDBUrl;

    @Value("${spring.datasource.druid.oa.xaProperties.username}")
    private String oaDBUser;

    @Value("${spring.datasource.druid.erp.xaProperties.url}")
    private String erpDBUrl;

    @Value("${spring.datasource.druid.erp.xaProperties.username}")
    private String erpDBUser;

    @Value("${sunten.oaUri}")
    private String oaUri;

    @Value("${sunten.hrmsVueIp}")
    private String hrmsVueIp;

    @Value("${sunten.hrmsVuePort}")
    private String hrmsVuePort;

    @Value("${sunten.mobilePlatformUrl}")
    private String mobilePlatformUrl;


    public StartupReport(ToolEmailInterfaceService toolEmailInterfaceService, RedisTemplate redisTemplate, WtaQuartzJobDao wtaQuartzJobDao) {
        this.toolEmailInterfaceService = toolEmailInterfaceService;
        this.redisTemplate = redisTemplate;
        this.wtaQuartzJobDao = wtaQuartzJobDao;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) {
        System.out.println("--------------------生成启动报告---------------------");
        LocalDateTime startupTime = LocalDateTime.now();

        Dict dict = Dict.create();

        // 题头信息
        dict.set("startupTime", startupTime);
        dict.set("serverUrl", toolEmailInterfaceService.getUrl());

        // 数据库列表
        List<Map<String, String>> dbInfos = new ArrayList<>();
        Map<String, String> hrmsMap = new HashMap<>();
        hrmsMap.put("resourceName", "hrms");
        hrmsMap.put("url", hrmsDBUrl);
        hrmsMap.put("user", hrmsDBUser);
        dbInfos.add(hrmsMap);

        Map<String, String> oaMap = new HashMap<>();
        oaMap.put("resourceName", "oa");
        oaMap.put("url", oaDBUrl);
        oaMap.put("user", oaDBUser);
        dbInfos.add(oaMap);

        Map<String, String> erpMap = new HashMap<>();
        erpMap.put("resourceName", "erp");
        erpMap.put("url", erpDBUrl);
        erpMap.put("user", erpDBUser);
        dbInfos.add(erpMap);

        dict.set("dbInfoList", dbInfos);

        List<Map<String, String>> oauthInfos = new ArrayList<>();
        Map<String, String> hrmsVueOauthMap = new HashMap<>();
        hrmsVueOauthMap.put("oauthName", "hrmsVue");
        hrmsVueOauthMap.put("url", hrmsVueIp + ":" + hrmsVuePort);
        oauthInfos.add(hrmsVueOauthMap);

        Map<String, String> hrmsOauthMap = new HashMap<>();
        hrmsOauthMap.put("oauthName", "oa");
        hrmsOauthMap.put("url", oaUri);
        oauthInfos.add(hrmsOauthMap);

        Map<String, String> mobilePlatformOauthMap = new HashMap<>();
        mobilePlatformOauthMap.put("oauthName", "mobilePlatform");
        mobilePlatformOauthMap.put("url", mobilePlatformUrl);
        oauthInfos.add(mobilePlatformOauthMap);

        dict.set("oauthInfoList", oauthInfos);

        // redis检查
        String redisStatus = "pass";
        String redisMessage = "正常";
        try {
            redisTemplate.opsForValue().set("HRMS::startupTime", startupTime);
        } catch (Exception ex) {
            redisStatus = "error";
            redisMessage = ThrowableUtil.getStackTrace(ex).replaceAll("[\\r]", "<br>");
        }
        dict.set("redisStatus", redisStatus);
        dict.set("redisMessage", redisMessage);

        // 邮件设置检查
        Map<String, String> checkResult = toolEmailInterfaceService.checkEmailSetting();
        dict.set("mailStatus", checkResult.get("status"));
        dict.set("mailMessage", checkResult.get("message"));
        String jobStatus = "pass";
        String jobMessage = "";
        try {
            if (scheduler.isStarted()) {
                jobStatus = "started";
            } else {
                if (scheduler.isInStandbyMode()) {
                    jobStatus = "standbyMode";
                } else {
                    if (scheduler.isShutdown()) {
                        jobStatus = "shutdown";
                    }
                }
            }
        } catch (SchedulerException ex) {
            jobStatus = "error";
            jobMessage = ThrowableUtil.getStackTrace(ex).replaceAll("[\\r]", "<br>");
        }

        // 定时任务检查
        dict.set("jobStatus", jobStatus);
        dict.set("jobMessage", jobMessage);
        dict.set("serverCheckStatus", GlobalVariablesSingleton.getInstance().getServerCheckStatus());
        dict.set("serverCheckMessage", GlobalVariablesSingleton.getInstance().getServerCheckMessage());
        if ("started".equals(jobStatus) && "pass".equals(GlobalVariablesSingleton.getInstance().getServerCheckStatus())) {
            List<WtaQuartzJob> enableJob = wtaQuartzJobDao.listEnableJob();
            dict.set("enableJobList", enableJob);
            List<WtaQuartzJob> pauseJob = wtaQuartzJobDao.listPauseJob();
            dict.set("pauseJobList", pauseJob);
        }

        // 套用模板生成邮件内容
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/startupReportEmail.ftl");
        String mailContent = template.render(dict);

        if (profilesActive.equals(startupReportSendProfiles)) {
            String sendToEmail = startupReportSendTo;
            ToolEmailServer emailServer = new ToolEmailServer();
            emailServer.setId(inSuntenEmailServerId);
            ToolEmailInterface emailInterface = new ToolEmailInterface();
            emailInterface.setEmailServer(emailServer);
            emailInterface.setMailSubject("HRMS系统启动报告");
            emailInterface.setMailContent(mailContent);
            emailInterface.setPlannedDate(startupTime);
            emailInterface.setSendTo(startupReportSendTo);
            toolEmailInterfaceService.sendAndSaveWithThrow(emailInterface, true);
        } else {
            System.out.println("当前使用配置文件" + profilesActive + ",与要求发送启动报表的配置文件" + startupReportSendProfiles + "不一致，仅在工作台显示启动报告！");
            System.out.println(mailContent);
        }

        System.out.println("--------------------生成启动报告完成---------------------");
    }
}
