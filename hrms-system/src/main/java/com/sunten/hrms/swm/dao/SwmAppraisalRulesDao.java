package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmAppraisalRules;
import com.sunten.hrms.swm.dto.SwmAppraisalRulesQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 考核规则 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmAppraisalRulesDao extends BaseMapper<SwmAppraisalRules> {

    int insertAllColumn(SwmAppraisalRules appraisalRules);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmAppraisalRules appraisalRules);

    int updateAllColumnByKey(SwmAppraisalRules appraisalRules);

    SwmAppraisalRules getByKey(@Param(value = "id") Long id);

    List<SwmAppraisalRules> listAllByCriteria(@Param(value = "criteria") SwmAppraisalRulesQueryCriteria criteria);

    List<SwmAppraisalRules> listAllByCriteriaPage(@Param(value = "page") Page<SwmAppraisalRules> page, @Param(value = "criteria") SwmAppraisalRulesQueryCriteria criteria);

    int InvalidByEnabled(SwmAppraisalRules appraisalRules);

    Integer checkByGrade(@Param(value = "assessmentGrade")String assessmentGrade);
}
