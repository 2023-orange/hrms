package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmMonthlyQuarterlyAssessment;
import com.sunten.hrms.swm.domain.SwmMonthlyQuarterlyAssessmentNum;
import com.sunten.hrms.swm.dto.SwmMonthlyQuarterlyAssessmentQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 月度考核表(一个季度生成三条月度) Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmMonthlyQuarterlyAssessmentDao extends BaseMapper<SwmMonthlyQuarterlyAssessment> {

    int insertAllColumn(SwmMonthlyQuarterlyAssessment monthlyQuarterlyAssessment);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmMonthlyQuarterlyAssessment monthlyQuarterlyAssessment);

    int updateAllColumnByKey(SwmMonthlyQuarterlyAssessment monthlyQuarterlyAssessment);

    SwmMonthlyQuarterlyAssessment getByKey(@Param(value = "id") Long id);

    List<SwmMonthlyQuarterlyAssessment> listAllByCriteria(@Param(value = "criteria") SwmMonthlyQuarterlyAssessmentQueryCriteria criteria);

    List<SwmMonthlyQuarterlyAssessment> listAllByCriteriaPage(@Param(value = "page") Page<SwmMonthlyQuarterlyAssessment> page, @Param(value = "criteria") SwmMonthlyQuarterlyAssessmentQueryCriteria criteria);

    void createMonthlyAssessment(Map<String, Object> map);

    int deleteMonthlyByPeriod(SwmMonthlyQuarterlyAssessment swmMonthlyQuarterlyAssessment);

    int deleteQuarterByPeriod(@Param(value = "incomePeriod") String period);

    String getTopMonthPeriod();

    Integer countByMonth(@Param(value = "period") String period);

    int updateAssessmentLevel(SwmMonthlyQuarterlyAssessment swmMonthlyQuarterlyAssessment);

    int updateQuarterSubMonthAssessmentLevel(
            @Param(value = "quarter" )String quarter,
                                             @Param(value = "periodList")List<String> periodList
                                            );

    List<SwmMonthlyQuarterlyAssessment> getAssessmentList(@Param(value = "workCard") String workCard);

    // 冻结
    void updateFlozenFlagByLimit(@Param(value = "updateLimit") Long updateLimit);

    void cancelFrozen(@Param(value = "workCard") String workCard,@Param(value = "assessmentMonth") String assessmentMonth,@Param(value = "frozenFlag") Boolean frozenFlag);

    Integer countByFrozenFlag(@Param(value = "period") String period);

    SwmMonthlyQuarterlyAssessmentNum countNumByWorkCard(@Param(value = "workCard") String workCard,@Param(value = "assessmentMonth") String assessmentMonth,@Param(value = "department") String department,@Param(value = "administrativeOffice") String administrativeOffice);

    SwmMonthlyQuarterlyAssessmentNum limitDept(SwmMonthlyQuarterlyAssessment swmMonthlyQuarterlyAssessment);

    SwmMonthlyQuarterlyAssessmentNum noLimitDept(SwmMonthlyQuarterlyAssessment swmMonthlyQuarterlyAssessment);

}
