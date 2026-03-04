package com.sunten.hrms.kpi.dao;

import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsInterface;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-12-20
 */
@Mapper
@Repository
public interface KpiAssessmentIndicatorsInterfaceDao extends BaseMapper<KpiAssessmentIndicatorsInterface> {

    int insertAllColumn(KpiAssessmentIndicatorsInterface assessmentIndicatorsInterface);

    int deleteByKey();

    int deleteByEntityKey(KpiAssessmentIndicatorsInterface assessmentIndicatorsInterface);

    int updateAllColumnByKey(KpiAssessmentIndicatorsInterface assessmentIndicatorsInterface);

    KpiAssessmentIndicatorsInterface getByKey();

    List<KpiAssessmentIndicatorsInterface> listAllByCriteria(@Param(value = "criteria") KpiAssessmentIndicatorsInterfaceQueryCriteria criteria);

    List<KpiAssessmentIndicatorsInterface> listAllByCriteriaPage(@Param(value = "page") Page<KpiAssessmentIndicatorsInterface> page, @Param(value = "criteria") KpiAssessmentIndicatorsInterfaceQueryCriteria criteria);

    void insertKpiAssessmentIndicatorsInterfaceToMain(Map<String, Object> map);

    void checkKpiAssessmentIndicatorsInterface(Map<String, Object> map);

    List<KpiAssessmentIndicatorsInterface> getKpiAssessmentIndicatorsInterfaceSummaryByImportList(@Param(value = "workCards") Set<String> workCards, @Param(value = "groupIds")Set<Long> groupIds);
}
