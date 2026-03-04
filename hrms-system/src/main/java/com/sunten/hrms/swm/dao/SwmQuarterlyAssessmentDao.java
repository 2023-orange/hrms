package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmQuarterlyAssessment;
import com.sunten.hrms.swm.dto.SwmQuarterlyAssessmentQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 季度考核表（一个季度生成一条，主要用作季度考核查询） Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmQuarterlyAssessmentDao extends BaseMapper<SwmQuarterlyAssessment> {

    int insertAllColumn(SwmQuarterlyAssessment quarterlyAssessment);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmQuarterlyAssessment quarterlyAssessment);

    int updateAllColumnByKey(SwmQuarterlyAssessment quarterlyAssessment);

    SwmQuarterlyAssessment getByKey(@Param(value = "id") Long id);

    List<SwmQuarterlyAssessment> listAllByCriteria(@Param(value = "criteria") SwmQuarterlyAssessmentQueryCriteria criteria);

    List<SwmQuarterlyAssessment> listAllByCriteriaPage(@Param(value = "page") Page<SwmQuarterlyAssessment> page, @Param(value = "criteria") SwmQuarterlyAssessmentQueryCriteria criteria);

    String getTopQuarterPeriod();

    void createQuarterlyAssessment(Map<String, Object> map);

    int deleteQuarterByPeriod(SwmQuarterlyAssessment swmQuarterlyAssessment);

    Integer countByQuarter(@Param(value = "quarter") String quarter);

    int updateQuarterAssessmentLevel(SwmQuarterlyAssessment swmQuarterlyAssessment);

    int interfaceToMain(@Param(value = "groupId")Long groupId);
}

