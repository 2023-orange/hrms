package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndLog;
import com.sunten.hrms.fnd.dto.FndLogDTO;
import com.sunten.hrms.fnd.dto.FndLogQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author batan
 * @since 2019-12-23
 */
public interface FndLogService extends IService<FndLog> {

    FndLogDTO insert(FndLog logNew);

    void delete(Long id);

    void delete(FndLog log);

    void update(FndLog logNew);

    FndLogDTO getByKey(Long id);

    List<FndLogDTO> listAll(FndLogQueryCriteria criteria);

    Map<String, Object> listAll(FndLogQueryCriteria criteria, Pageable pageable);

    Object listAllByUser(FndLogQueryCriteria criteria, Pageable pageable);

    void download(List<FndLogDTO> logDTOS, HttpServletResponse response) throws IOException;

    @Async
    void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint, FndLog log);

    @Async
    void saveError(String username, String browser, String ip, ProceedingJoinPoint joinPoint, FndLog log);

    /**
     * 查询异常详情
     * @param id 日志ID
     * @return Object
     */
    Object listByErrDetail(Long id);
}
