package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdTrainingEvaluationScore;
import com.sunten.hrms.td.dto.TdTrainingEvaluationScoreDTO;
import com.sunten.hrms.td.dto.TdTrainingEvaluationScoreQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训评价分数表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2022-03-10
 */
public interface TdTrainingEvaluationScoreService extends IService<TdTrainingEvaluationScore> {

    TdTrainingEvaluationScoreDTO insert(TdTrainingEvaluationScore trainingEvaluationScoreNew);

    void delete(Long id);

    void delete(TdTrainingEvaluationScore trainingEvaluationScore);

    void update(TdTrainingEvaluationScore trainingEvaluationScoreNew);

    TdTrainingEvaluationScoreDTO getByKey(Long id);

    List<TdTrainingEvaluationScoreDTO> listAll(TdTrainingEvaluationScoreQueryCriteria criteria);

    Map<String, Object> listAll(TdTrainingEvaluationScoreQueryCriteria criteria, Pageable pageable);

    void download(List<TdTrainingEvaluationScoreDTO> trainingEvaluationScoreDTOS, HttpServletResponse response) throws IOException;
}
