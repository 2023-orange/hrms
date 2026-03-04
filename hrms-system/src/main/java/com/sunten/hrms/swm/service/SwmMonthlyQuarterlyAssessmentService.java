package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmMonthlyQuarterlyAssessment;
import com.sunten.hrms.swm.domain.SwmMonthlyQuarterlyAssessmentNum;
import com.sunten.hrms.swm.dto.SwmMonthlyQuarterlyAssessmentDTO;
import com.sunten.hrms.swm.dto.SwmMonthlyQuarterlyAssessmentQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 月度考核表(一个季度生成三条月度) 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmMonthlyQuarterlyAssessmentService extends IService<SwmMonthlyQuarterlyAssessment> {

    SwmMonthlyQuarterlyAssessmentDTO insert(SwmMonthlyQuarterlyAssessment monthlyQuarterlyAssessmentNew);

    void delete(Long id);

    void delete(SwmMonthlyQuarterlyAssessment monthlyQuarterlyAssessment);

    void update(SwmMonthlyQuarterlyAssessment monthlyQuarterlyAssessmentNew);

    SwmMonthlyQuarterlyAssessmentDTO getByKey(Long id);

    List<SwmMonthlyQuarterlyAssessmentDTO> listAll(SwmMonthlyQuarterlyAssessmentQueryCriteria criteria);

    Map<String, Object> listAll(SwmMonthlyQuarterlyAssessmentQueryCriteria criteria, Pageable pageable);

    void download(List<SwmMonthlyQuarterlyAssessmentDTO> monthlyQuarterlyAssessmentDTOS, HttpServletResponse response) throws IOException;

    List<SwmMonthlyQuarterlyAssessment> createMonthlyAssessment(String period);

    void removeMonthlyByPeriodWithNoCheck(String period);

    void removeMonthlyByPeriod(String period);

    void removeQuarterlyByPeriod(String period);

    List<String> getMonthPeriodList();

//    String submitEmployeeMonthly(List<SwmMonthlyQuarterlyAssessment> swmMonthlyQuarterlyAssessments);

    void submitEmployeeMonthly(List<SwmMonthlyQuarterlyAssessment> swmMonthlyQuarterlyAssessments);

    void updateQuarterSubMonthAssessmentLevel( List<String> periodList,String quarter);

    List<SwmMonthlyQuarterlyAssessmentDTO>  getAssessmentList(String workCard);

    void flozenMonthAppraisalSalary(Long limit, String period);

    void cancelFrozen(String workCard,String assessmentMonth,Boolean frozenFlag);
}
