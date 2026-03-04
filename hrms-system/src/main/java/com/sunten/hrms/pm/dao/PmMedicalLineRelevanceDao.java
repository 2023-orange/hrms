package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmMedicalLineRelevance;
import com.sunten.hrms.pm.dto.PmMedicalLineRelevanceQueryCriteria;
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
 * @author xukai
 * @since 2021-04-20
 */
@Mapper
@Repository
public interface PmMedicalLineRelevanceDao extends BaseMapper<PmMedicalLineRelevance> {

    int insertAllColumn(PmMedicalLineRelevance medicalLineRelevance);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmMedicalLineRelevance medicalLineRelevance);

    int updateAllColumnByKey(PmMedicalLineRelevance medicalLineRelevance);

    PmMedicalLineRelevance getByKey(@Param(value = "id") Long id);

    List<PmMedicalLineRelevance> listAllByCriteria(@Param(value = "criteria") PmMedicalLineRelevanceQueryCriteria criteria);

    List<PmMedicalLineRelevance> listAllByCriteriaPage(@Param(value = "page") Page<PmMedicalLineRelevance> page, @Param(value = "criteria") PmMedicalLineRelevanceQueryCriteria criteria);
    // 删除信息信息对应的体检项
    int deleteByMedicalLineId(@Param(value = "medicalLineId") Long medicalLineId);
    // 批量新增
    int batchInsertAllColumn(@Param(value = "relevances") List<PmMedicalLineRelevance> relevances);
    // 批量删除部分体检项
    int batchDeleteByList(@Param(value = "relevances") List<PmMedicalLineRelevance> relevances);
    // 根据体检薪资子表id批量删除体检项
    int batchDeleteByMedicalLineId(@Param(value = "medicalLineId") Long medicalLineId);
}
