package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdPlan;
import com.sunten.hrms.td.domain.TdPlanInterface;
import com.sunten.hrms.td.domain.TdSafetyTraining;
import com.sunten.hrms.td.domain.TdSafetyTrainingDept;
import com.sunten.hrms.td.dto.TdPlanQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 培训计划表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-05-19
 */
@Mapper
@Repository
public interface TdPlanDao extends BaseMapper<TdPlan> {

    int insertAllColumn(TdPlan plan);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdPlan plan);

    int updateAllColumnByKey(TdPlan plan);

    TdPlan getByKey(@Param(value = "id") Long id);

    List<TdPlan> listAllByCriteria(@Param(value = "criteria") TdPlanQueryCriteria criteria);

    List<TdPlan> listAllByCriteriaPage(@Param(value = "page") Page<TdPlan> page, @Param(value = "criteria") TdPlanQueryCriteria criteria);

    int interfaceToMain(@Param(value = "groupId") Long id);

    TdPlan getPlanAndImpementByPlanId(@Param(value = "planId") Long planId);

    TdPlan getPlanAndImpementByOaOrder(@Param(value = "oaOrder") String oaOrder);

    TdPlan getPlanByChangeOaOrder(@Param(value = "changeOaOrder") String changeOaOrder);

    int setShowFlagAfterImplementPass(@Param(value = "id") Long id);

    int interfaceToMainByObj(TdPlanInterface tdPlanInterface);

    Integer getNumberByMethod(@Param(value = "trainingMethod") String traningMethod);

    // 安全培训
    List<TdSafetyTraining> getSafetyTrainingByCriteria(@Param(value = "criteria") TdPlanQueryCriteria criteria);

    List<TdSafetyTraining> getSafetyTrainingByCriteriaPage(@Param(value = "page") Page<TdSafetyTraining> page, @Param(value = "criteria") TdPlanQueryCriteria criteria);

    int insertSafetyTraining(TdSafetyTraining tdSafetyTraining);

    int updateSafetyTraining(TdSafetyTraining tdSafetyTraining);

    List<TdSafetyTrainingDept> getSafetyTrainingDept();
}
