package com.sunten.hrms.fnd.aspect;

import com.sunten.hrms.exception.EntityExistException;
import com.sunten.hrms.exception.EntityNotFoundException;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndLog;
import com.sunten.hrms.fnd.service.FndLogService;
import com.sunten.hrms.utils.RequestHolder;
import com.sunten.hrms.utils.SecurityUtils;
import com.sunten.hrms.utils.StringUtils;
import com.sunten.hrms.utils.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author batan
 * @since 2018-11-24
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    private final FndLogService fndLogService;

    private long currentTime = 0L;

    public LogAspect(FndLogService fndLogService) {
        this.fndLogService = fndLogService;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.sunten.hrms.fnd.aop.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.sunten.hrms.fnd.aop.ErrorLog)")
    public void errorLogPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime = System.currentTimeMillis();
        result = joinPoint.proceed();
        FndLog log = new FndLog("INFO", System.currentTimeMillis() - currentTime);
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        fndLogService.save(getUsername(), StringUtils.getBrowser(request), StringUtils.getIp(request), joinPoint, log);
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (!e.getClass().equals(InfoCheckWarningMessException.class) && !e.getClass().equals(EntityNotFoundException.class) && !e.getClass().equals(EntityExistException.class)) {
            FndLog log = new FndLog("ERROR", System.currentTimeMillis() - currentTime);
            log.setExceptionDetail(ThrowableUtil.getStackTrace(e));
            HttpServletRequest request = RequestHolder.getHttpServletRequest();
            fndLogService.save(getUsername(), StringUtils.getBrowser(request), StringUtils.getIp(request), (ProceedingJoinPoint) joinPoint, log);
        }
    }


    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "errorLogPointcut()", throwing = "e")
    public void errorLogAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (!e.getClass().equals(InfoCheckWarningMessException.class) && !e.getClass().equals(EntityNotFoundException.class) && !e.getClass().equals(EntityExistException.class)) {
            FndLog log = new FndLog("ERROR", System.currentTimeMillis() - currentTime);
            log.setExceptionDetail(ThrowableUtil.getStackTrace(e));
            HttpServletRequest request = RequestHolder.getHttpServletRequest();
            fndLogService.saveError(getUsername(), StringUtils.getBrowser(request), StringUtils.getIp(request), (ProceedingJoinPoint) joinPoint, log);
        }
    }

    public String getUsername() {
        try {
            return SecurityUtils.getUsername();
        } catch (Exception e) {
            return "";
        }
    }
}
