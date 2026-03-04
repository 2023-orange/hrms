package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdPlanInspectionSituation;
import com.sunten.hrms.td.dto.TdPlanInspectionSituationDTO;
import com.sunten.hrms.td.dto.TdPlanInspectionSituationQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训考核情况 服务类
 * </p>
 *
 * @author liangjw
 * @since 2022-03-11
 */
public interface TdPlanInspectionSituationService extends IService<TdPlanInspectionSituation> {

    TdPlanInspectionSituationDTO insert(TdPlanInspectionSituation planInspectionSituationNew);

    void delete(Long id);

    void delete(TdPlanInspectionSituation planInspectionSituation);

    void update(TdPlanInspectionSituation planInspectionSituationNew);

    TdPlanInspectionSituationDTO getByKey(Long id);

    List<TdPlanInspectionSituationDTO> listAll(TdPlanInspectionSituationQueryCriteria criteria);

    Map<String, Object> listAll(TdPlanInspectionSituationQueryCriteria criteria, Pageable pageable);

    void download(List<TdPlanInspectionSituationDTO> planInspectionSituationDTOS, HttpServletResponse response) throws IOException;
}
