package com.sunten.hrms.kpi.dao;

import com.sunten.hrms.kpi.domain.KpiAnnual;
import com.sunten.hrms.kpi.dto.KpiAnnualQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * KPI考核年度概况 Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
@Mapper
@Repository
public interface KpiAnnualDao extends BaseMapper<KpiAnnual> {

    int insertAllColumn(KpiAnnual annual);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(KpiAnnual annual);

    int updateAllColumnByKey(KpiAnnual annual);

    KpiAnnual getByKey(@Param(value = "id") Long id);

    List<KpiAnnual> listAllByCriteria(@Param(value = "criteria") KpiAnnualQueryCriteria criteria);

    List<KpiAnnual> listAllByCriteriaPage(@Param(value = "page") Page<KpiAnnual> page, @Param(value = "criteria") KpiAnnualQueryCriteria criteria);

    Integer getMaxYear();

    Integer getEnabledAnnual();

    Integer getEnabledAnnualByYear(@Param(value = "criteria") KpiAnnualQueryCriteria criteria);

    Integer getDataBeginByYear(@Param(value = "criteria") KpiAnnualQueryCriteria criteria);
}
