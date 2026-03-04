package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmAppraisalRules;
import com.sunten.hrms.swm.dto.SwmAppraisalRulesDTO;
import com.sunten.hrms.swm.dto.SwmAppraisalRulesQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 考核规则 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmAppraisalRulesService extends IService<SwmAppraisalRules> {

    SwmAppraisalRulesDTO insert(SwmAppraisalRules appraisalRulesNew);

    void delete(Long id);

    void delete(SwmAppraisalRules appraisalRules);

    void update(SwmAppraisalRules appraisalRulesNew);

    SwmAppraisalRulesDTO getByKey(Long id);

    List<SwmAppraisalRulesDTO> listAll(SwmAppraisalRulesQueryCriteria criteria);

    Map<String, Object> listAll(SwmAppraisalRulesQueryCriteria criteria, Pageable pageable);

    void download(List<SwmAppraisalRulesDTO> appraisalRulesDTOS, HttpServletResponse response) throws IOException;

    void updateByWorkFlag(SwmAppraisalRules swmAppraisalRules);

//    Integer checkByGrade(String assessmentGrade);
}
