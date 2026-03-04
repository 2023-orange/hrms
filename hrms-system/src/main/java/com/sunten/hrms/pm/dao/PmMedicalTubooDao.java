package com.sunten.hrms.pm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.pm.domain.PmMedicalTuboo;
import com.sunten.hrms.pm.dto.PmMedicalTubooQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2022-11-30
 */
@Mapper
@Repository
public interface PmMedicalTubooDao extends BaseMapper<PmMedicalTuboo> {

    int insertAllColumn(PmMedicalTuboo pmMedicalTuboo);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmMedicalTuboo pmMedicalTuboo);

    int updateAllColumnByKey(PmMedicalTuboo pmMedicalTuboo);

    PmMedicalTuboo getByKey(@Param(value = "id") Long id);

    List<PmMedicalTuboo> listAllByCriteria(@Param(value = "criteria") PmMedicalTubooQueryCriteria criteria);

    List<PmMedicalTuboo> listAllByCriteriaPage(@Param(value = "page") Page<PmMedicalTuboo> page, @Param(value = "criteria") PmMedicalTubooQueryCriteria criteria);

    List<PmMedicalTuboo> getPmMedicalTubooSub(@Param(value = "workCard") String workCard);
}
