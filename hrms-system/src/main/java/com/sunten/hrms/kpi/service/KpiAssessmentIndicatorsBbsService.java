package com.sunten.hrms.kpi.service;

import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsBbs;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsBbsDTO;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsBbsQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-12-26
 */
public interface KpiAssessmentIndicatorsBbsService extends IService<KpiAssessmentIndicatorsBbs> {

    KpiAssessmentIndicatorsBbsDTO insert(KpiAssessmentIndicatorsBbs assessmentIndicatorsBbsNew);

    void delete();

    void delete(KpiAssessmentIndicatorsBbs assessmentIndicatorsBbs);

    void update(KpiAssessmentIndicatorsBbs assessmentIndicatorsBbsNew);

    KpiAssessmentIndicatorsBbsDTO getByKey();

    List<KpiAssessmentIndicatorsBbsDTO> listAll(KpiAssessmentIndicatorsBbsQueryCriteria criteria);

    Map<String, Object> listAll(KpiAssessmentIndicatorsBbsQueryCriteria criteria, Pageable pageable);

    void download(List<KpiAssessmentIndicatorsBbsDTO> assessmentIndicatorsBbsDTOS, HttpServletResponse response) throws IOException;
}
