package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdTrainingEvaluationScore;
import com.sunten.hrms.td.dto.TdTrainingEvaluationScoreQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训评价分数表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2022-03-10
 */
@Mapper
@Repository
public interface TdTrainingEvaluationScoreDao extends BaseMapper<TdTrainingEvaluationScore> {

    int insertAllColumn(TdTrainingEvaluationScore trainingEvaluationScore);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdTrainingEvaluationScore trainingEvaluationScore);

    int updateAllColumnByKey(TdTrainingEvaluationScore trainingEvaluationScore);

    TdTrainingEvaluationScore getByKey(@Param(value = "id") Long id);

    List<TdTrainingEvaluationScore> listAllByCriteria(@Param(value = "criteria") TdTrainingEvaluationScoreQueryCriteria criteria);

    List<TdTrainingEvaluationScore> listAllByCriteriaPage(@Param(value = "page") Page<TdTrainingEvaluationScore> page, @Param(value = "criteria") TdTrainingEvaluationScoreQueryCriteria criteria);
}
