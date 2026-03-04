package com.sunten.hrms.kpi.service;

import com.sunten.hrms.kpi.domain.KpiAssessmentDimension;
import com.sunten.hrms.kpi.dto.KpiAssessmentDimensionDTO;
import com.sunten.hrms.kpi.dto.KpiAssessmentDimensionQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * KPI考核维度表 服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
public interface KpiAssessmentDimensionService extends IService<KpiAssessmentDimension> {

    KpiAssessmentDimensionDTO insert(KpiAssessmentDimension assessmentDimensionNew);

    void delete(Long id);

    void delete(KpiAssessmentDimension assessmentDimension);

    void update(KpiAssessmentDimension assessmentDimensionNew);

    KpiAssessmentDimensionDTO getByKey(Long id);

    List<KpiAssessmentDimensionDTO> listAll(KpiAssessmentDimensionQueryCriteria criteria);

    Map<String, Object> listAll(KpiAssessmentDimensionQueryCriteria criteria, Pageable pageable);

    void download(List<KpiAssessmentDimensionDTO> assessmentDimensionDTOS, HttpServletResponse response) throws IOException;

    List<KpiAssessmentDimension> listAllByEnableFlag();
}
