package com.sunten.hrms.kpi.service;

import com.sunten.hrms.kpi.domain.KpiAssessmentDimensionDept;
import com.sunten.hrms.kpi.dto.KpiAssessmentDimensionDeptDTO;
import com.sunten.hrms.kpi.dto.KpiAssessmentDimensionDeptQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * KPI考核维度与部门关系表 服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-28
 */
public interface KpiAssessmentDimensionDeptService extends IService<KpiAssessmentDimensionDept> {

    KpiAssessmentDimensionDeptDTO insert(KpiAssessmentDimensionDept assessmentDimensionDeptNew);

    void delete(Long id);

    void delete(KpiAssessmentDimensionDept assessmentDimensionDept);

    void update(KpiAssessmentDimensionDept assessmentDimensionDeptNew);

    KpiAssessmentDimensionDeptDTO getByKey(Long id);

    List<KpiAssessmentDimensionDeptDTO> listAll(KpiAssessmentDimensionDeptQueryCriteria criteria);

    Map<String, Object> listAll(KpiAssessmentDimensionDeptQueryCriteria criteria, Pageable pageable);

    void download(List<KpiAssessmentDimensionDeptDTO> assessmentDimensionDeptDTOS, HttpServletResponse response) throws IOException;

    List<KpiAssessmentDimensionDeptDTO> listMultipleChoice(@Param(value = "id") Long id);
}
