package com.sunten.hrms.td.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.td.dto.TdTrainingEvaluationScoreDTO;
import com.sunten.hrms.td.domain.TdTrainingEvaluationScore;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2022-03-10
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TdTrainingEvaluationScoreMapper extends BaseMapper<TdTrainingEvaluationScoreDTO, TdTrainingEvaluationScore> {

}
