package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdPlan;
import com.sunten.hrms.td.domain.TdPlanInterface;
import com.sunten.hrms.td.domain.TdSafetyTraining;
import com.sunten.hrms.td.dto.TdPlanDTO;
import com.sunten.hrms.td.dto.TdPlanQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.td.vo.SafetyTrainingDeptVo;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>
 * 培训计划表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-05-19
 */
public interface TdPlanService extends IService<TdPlan> {

    TdPlanDTO insert(TdPlan planNew);

    void delete(Long id);

    void delete(TdPlan plan);

    void update(TdPlan planNew);

    TdPlanDTO getByKey(Long id);

    List<TdPlanDTO> listAll(TdPlanQueryCriteria criteria);

    Map<String, Object> listAll(TdPlanQueryCriteria criteria, Pageable pageable);

    void download(List<TdPlanDTO> planDTOS, HttpServletResponse response) throws IOException;

    void interfaceToMain(Long groupId);

    void interfaceToMainByObj(TdPlanInterface tdPlanInterface);

    TdPlanDTO getPlanAndImpementByPlanId(Long planId);

    TdPlanDTO getPlanAndImpementByOaOrder(String oaOrder);

    TdPlanDTO getPlanByChangeOaOrder(String changeOaOrder);

    void updateShowFlagAfterImplementPass(Long id);

    TdPlanDTO insertPlanAndImplementAndOther(TdPlan plan);

    List<TdSafetyTraining> listSafetyTrainingByCriteria(TdPlanQueryCriteria criteria);

    Map<String, Object> listSafetyTrainingByCriteriaPage(TdPlanQueryCriteria criteria, Pageable pageable);

    void insertSafetyTraining(TdSafetyTraining tdSafetyTraining);

    void updateSafetyTraining(TdSafetyTraining tdSafetyTraining);

    Set<SafetyTrainingDeptVo> getSafetyTrainingDeptVo();
}
