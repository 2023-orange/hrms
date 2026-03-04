package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmQuarterlyAssessment;
import com.sunten.hrms.swm.dto.SwmQuarterlyAssessmentDTO;
import com.sunten.hrms.swm.dto.SwmQuarterlyAssessmentQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 季度考核表（一个季度生成一条，主要用作季度考核查询） 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmQuarterlyAssessmentService extends IService<SwmQuarterlyAssessment> {

    SwmQuarterlyAssessmentDTO insert(SwmQuarterlyAssessment quarterlyAssessmentNew);

    void delete(Long id);

    void delete(SwmQuarterlyAssessment quarterlyAssessment);

    void update(SwmQuarterlyAssessment quarterlyAssessmentNew);

    SwmQuarterlyAssessmentDTO getByKey(Long id);

    List<SwmQuarterlyAssessmentDTO> listAll(SwmQuarterlyAssessmentQueryCriteria criteria);

    Map<String, Object> listAll(SwmQuarterlyAssessmentQueryCriteria criteria, Pageable pageable);

    void download(List<SwmQuarterlyAssessmentDTO> quarterlyAssessmentDTOS, HttpServletResponse response) throws IOException;

    List<String> getQuarterPeriodList(String topPeriod);

    List<SwmQuarterlyAssessment> createQuarterlyAssessment(String period);

    void removeQuarterlyByPeriod(String period);

    void batchUpdateQuarterAssessmentLevel(List<SwmQuarterlyAssessment> swmQuarterlyAssessments);

    void interfaceToMain(Long groupId);

}
