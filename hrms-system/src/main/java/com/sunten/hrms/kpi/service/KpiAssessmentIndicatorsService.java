package com.sunten.hrms.kpi.service;

import com.sunten.hrms.kpi.domain.KpiAssessmentIndicators;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsDTO;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * KPI考核指标表 服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-28
 */
public interface KpiAssessmentIndicatorsService extends IService<KpiAssessmentIndicators> {

    KpiAssessmentIndicatorsDTO insert(KpiAssessmentIndicators assessmentIndicatorsNew);

    void delete(Long id);

    void delete(KpiAssessmentIndicators assessmentIndicators);

    void update(KpiAssessmentIndicators assessmentIndicatorsNew);

    KpiAssessmentIndicatorsDTO getByKey(Long id);

    List<KpiAssessmentIndicatorsDTO> listAll(KpiAssessmentIndicatorsQueryCriteria criteria);

    Map<String, Object> listAll(KpiAssessmentIndicatorsQueryCriteria criteria, Pageable pageable);

    List<KpiAssessmentIndicatorsDTO> listAssessedIndicators(KpiAssessmentIndicatorsQueryCriteria criteria);

    Map<String, Object> listAssessedIndicators(KpiAssessmentIndicatorsQueryCriteria criteria, Pageable pageable);

    void download(List<KpiAssessmentIndicatorsDTO> assessmentIndicatorsDTOS, HttpServletResponse response) throws IOException;

    List<Integer> getYearList();

    // 调用存储过程生成流水号
    Long createSerialNumberKpi(String year);

    List<HashMap<String, Object>> getAssessmentIndicatorsInfoByManger(String year);

    List<HashMap<String, Object>> getAssessmentIndicatorsInfoByDepartmentHead(String year);

    Double getResidueWeight(Long id);
}
