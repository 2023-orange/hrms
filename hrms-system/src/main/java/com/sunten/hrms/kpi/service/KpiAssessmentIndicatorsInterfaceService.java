package com.sunten.hrms.kpi.service;

import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsInterface;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsInterfaceDTO;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-12-20
 */
public interface KpiAssessmentIndicatorsInterfaceService extends IService<KpiAssessmentIndicatorsInterface> {

    KpiAssessmentIndicatorsInterfaceDTO insert(KpiAssessmentIndicatorsInterface assessmentIndicatorsInterfaceNew);

    void delete();

    void delete(KpiAssessmentIndicatorsInterface assessmentIndicatorsInterface);

    void update(KpiAssessmentIndicatorsInterface assessmentIndicatorsInterfaceNew);

    KpiAssessmentIndicatorsInterfaceDTO getByKey();

    List<KpiAssessmentIndicatorsInterfaceDTO> listAll(KpiAssessmentIndicatorsInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(KpiAssessmentIndicatorsInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<KpiAssessmentIndicatorsInterfaceDTO> assessmentIndicatorsInterfaceDTOS, HttpServletResponse response) throws IOException;

    List<KpiAssessmentIndicatorsInterfaceDTO> importExcel(List<KpiAssessmentIndicatorsInterface> kpiAssessmentIndicatorsInterfaces);

    List<KpiAssessmentIndicatorsInterface> getKpiAssessmentIndicatorsInterfaceSummaryByImportList(Set<String> workCards, Set<Long> groupIds);
}
