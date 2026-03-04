package com.sunten.hrms.kpi.service;

import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsMonth;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsMonthDTO;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsMonthQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * KPI考核指标子表 服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-28
 */
public interface KpiAssessmentIndicatorsMonthService extends IService<KpiAssessmentIndicatorsMonth> {

    KpiAssessmentIndicatorsMonthDTO insert(KpiAssessmentIndicatorsMonth assessmentIndicatorsMonthNew);

    void delete(Long id);

    void delete(KpiAssessmentIndicatorsMonth assessmentIndicatorsMonth);

    void update(KpiAssessmentIndicatorsMonth assessmentIndicatorsMonthNew);

    KpiAssessmentIndicatorsMonthDTO getByKey(Long id);

    List<KpiAssessmentIndicatorsMonthDTO> listAll(KpiAssessmentIndicatorsMonthQueryCriteria criteria);

    Map<String, Object> listAll(KpiAssessmentIndicatorsMonthQueryCriteria criteria, Pageable pageable);

    void download(List<KpiAssessmentIndicatorsMonthDTO> assessmentIndicatorsMonthDTOS, HttpServletResponse response) throws IOException;
}
