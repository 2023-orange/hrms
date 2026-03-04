package com.sunten.hrms.wta.utils;

import com.sunten.config.thread.ThreadPoolExecutorUtil;
import com.sunten.hrms.utils.SpringContextHolder;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.wta.dao.WtaQuartzLogDao;
import com.sunten.hrms.wta.domain.WtaQuartzJob;
import com.sunten.hrms.wta.domain.WtaQuartzLog;
import com.sunten.hrms.wta.service.WtaQuartzJobService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 参考人人开源，https://gitee.com/renrenio/renren-security
 *
 * @author /
 * @since 2019-01-07
 */
@Async
public class ExecutionJob extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 该处仅供参考
    private final static ThreadPoolExecutor executor = ThreadPoolExecutorUtil.getPoll();

    @Override
    @SuppressWarnings("unchecked")
    protected void executeInternal(JobExecutionContext context) {
        WtaQuartzJob quartzJob = (WtaQuartzJob) context.getMergedJobDataMap().get(WtaQuartzJob.JOB_KEY);
        // 获取spring bean
        WtaQuartzLogDao wtaQuartzLogDao = SpringContextHolder.getBean(WtaQuartzLogDao.class);
        WtaQuartzJobService wtaQuartzJobService = SpringContextHolder.getBean(WtaQuartzJobService.class);

        WtaQuartzLog log = new WtaQuartzLog();
        String jobName = quartzJob.getJobName();
        InetAddress localhost;
        try {
            localhost = InetAddress.getLocalHost();
            logger.info("hostName:" + localhost.getHostName());
            logger.info("hostAddress:" + localhost.getHostAddress());
            jobName = jobName + "(" + localhost.getHostName() + "[" + localhost.getHostAddress() + "])";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        log.setJobName(jobName.substring(0, Math.min(255, jobName.length())));
        log.setBeanName(quartzJob.getBeanName());
        log.setMethodName(quartzJob.getMethodName());
        log.setParams(quartzJob.getParams());
        long startTime = System.currentTimeMillis();
        log.setCronExpression(quartzJob.getCronExpression());
        try {
            // 执行任务
            logger.info("任务准备执行，任务名称：{}", quartzJob.getJobName());
            QuartzRunnable task = new QuartzRunnable(quartzJob.getBeanName(), quartzJob.getMethodName(),
                    quartzJob.getParams());
            Future<?> future = executor.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态
            log.setSuccess(true);
            logger.info("任务执行完毕，任务名称：{} 总共耗时：{} 毫秒", quartzJob.getJobName(), times);
        } catch (Exception e) {
            logger.error("任务执行失败，任务名称：{}", quartzJob.getJobName(), e);
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态 0：成功 1：失败
            log.setSuccess(false);
            log.setExceptionDetail(ThrowableUtil.getStackTrace(e));
//            quartzJob.setPause(false);
            //更新状态
//            wtaQuartzJobService.updatePause(quartzJob);
        } finally {
            wtaQuartzLogDao.insertAllColumn(log);
        }
    }
}
