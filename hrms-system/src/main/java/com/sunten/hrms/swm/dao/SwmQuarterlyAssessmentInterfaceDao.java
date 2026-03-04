package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmQuarterlyAssessmentInterface;
import com.sunten.hrms.swm.dto.SwmQuarterlyAssessmentInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2022-05-13
 */
@Mapper
@Repository
public interface SwmQuarterlyAssessmentInterfaceDao extends BaseMapper<SwmQuarterlyAssessmentInterface> {

    int insertAllColumn(SwmQuarterlyAssessmentInterface quarterlyAssessmentInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmQuarterlyAssessmentInterface quarterlyAssessmentInterface);

    int updateAllColumnByKey(SwmQuarterlyAssessmentInterface quarterlyAssessmentInterface);

    SwmQuarterlyAssessmentInterface getByKey(@Param(value = "id") Long id);

    List<SwmQuarterlyAssessmentInterface> listAllByCriteria(@Param(value = "criteria") SwmQuarterlyAssessmentInterfaceQueryCriteria criteria);

    List<SwmQuarterlyAssessmentInterface> listAllByCriteriaPage(@Param(value = "page") Page<SwmQuarterlyAssessmentInterface> page, @Param(value = "criteria") SwmQuarterlyAssessmentInterfaceQueryCriteria criteria);

    int insertByInterface(SwmQuarterlyAssessmentInterface swmQuarterlyAssessmentInterface);
}
