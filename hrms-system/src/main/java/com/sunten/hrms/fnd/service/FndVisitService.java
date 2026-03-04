package com.sunten.hrms.fnd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.fnd.domain.FndVisit;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author batan
 * @since 2019-12-20
 */
public interface FndVisitService extends IService<FndVisit> {
    /**
     * 提供给定时任务，每天0点执行
     */
    void save();

    /**
     * 新增记录
     * @param request /
     */
    @Async
    void count(HttpServletRequest request);

    /**
     * 获取数据
     * @return /
     */
    Object get();

    /**
     * getChartData
     * @return /
     */
    Object getChartData();
}
