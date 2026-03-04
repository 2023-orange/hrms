package com.sunten.hrms.ac.dao;

import com.sunten.config.DataSourceKeyEnum;
import com.sunten.config.annotation.DataSource;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zouyp
 */

@Mapper
@Repository
@DataSource(DataSourceKeyEnum.OA)
public interface AcOaLeaveDao {
    /**
     * 获取用户已使用年假总天数
     * @param workCard
     * @return
     */
    Double getUsedAnnualLeaveDays(@Param(value = "workCard") String workCard);

    /**
     * 获取用户已使用事假总天数
     * @param workCard
     * @return
     */
    Double getUsedLeaveOfAbsenceDays(@Param(value = "workCard") String workCard);

    /**
     * 获取员工月度加班工时
     * @param workCard
     * @return
     */
    Double getMonthOverTime(@Param(value = "workCard") String workCard);

    /**
     * 获取员工月度已休工时
     * @param workCard
     * @return
     */
    Double getUsedMonthOverTime(@Param(value = "workCard") String workCard);

    /**
     * 根据工牌号获取员工信息
     * @param workCard
     * @return
     */
    HashMap<String, String> getUserViews(@Param(value = "workCard") String workCard);

    /**
     * 去OA库查询上级领导列表
     * @param keyword
     * @return
     */
    @MapKey("ValueField")
    Map<String, Object> getDirectorMap(@Param("keyword") String keyword);

    /**
     * 创建OA申请单号
     * @param typeCode
     * @return
     */
    Object createOaReqCode(@Param("typeCode") String typeCode);

    /**
     * 根据工牌号和OA申请单号获取附件列表
     * @param workCard
     * @param oaOrder
     * @return
     */
    List<HashMap<String, Object>> getFileList(@Param("workCard") String workCard, @Param("oaOrder") String oaOrder);

    /**
     * 根据OA申请号去查询当前请假流程对应的taskID和processID
     * @param oaOrder
     * @return
     */
    HashMap<String, String> getFlowInfo(@Param("oaOrder") String oaOrder);
}
