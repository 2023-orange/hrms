package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmMedicalRelevance;
import com.sunten.hrms.pm.dto.PmMedicalRelevanceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 体检项目关联表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-04-19
 */
@Mapper
@Repository
public interface PmMedicalRelevanceDao extends BaseMapper<PmMedicalRelevance> {

    int insertAllColumn(PmMedicalRelevance medicalRelevance);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmMedicalRelevance medicalRelevance);

    int updateAllColumnByKey(PmMedicalRelevance medicalRelevance);

    PmMedicalRelevance getByKey(@Param(value = "id") Long id);

    List<PmMedicalRelevance> listAllByCriteria(@Param(value = "criteria") PmMedicalRelevanceQueryCriteria criteria);

    List<PmMedicalRelevance> listAllByCriteriaPage(@Param(value = "page") Page<PmMedicalRelevance> page, @Param(value = "criteria") PmMedicalRelevanceQueryCriteria criteria);
    // 删除特定体检项下的所有关联
    int deleteRelevanceByProjectId(@Param(value = "projectId") Long projectId);

    // 删除特定岗位下的所有关联
    int deleteRelevanceByJobId(@Param(value = "medicalJobId") Long medicalJobId);
    // 批量插入
    int batchInsertAllColumn(@Param(value = "medicalRelevances")List<PmMedicalRelevance> medicalRelevances);

    // 删除特定的关联
    int bathcdeleteRelevances(@Param(value = "medicalRelevances")List<PmMedicalRelevance> medicalRelevances);
}
