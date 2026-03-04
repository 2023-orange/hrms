package com.sunten.hrms.pm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.pm.domain.PmTrialAssess;
import com.sunten.hrms.pm.dto.PmTrialAssessDTO;
import com.sunten.hrms.pm.dto.PmTrialAssessQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * 新晋员工试用期考核表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-05-07
 */
public interface PmTrialAssessService extends IService<PmTrialAssess> {

    PmTrialAssessDTO insert(PmTrialAssess trialAssessNew);

    void delete(Long id);

    void delete(PmTrialAssess trialAssess);

    void update(PmTrialAssess trialAssessNew);

    void submitWorkSummary(PmTrialAssess trialAssessNew);

    void updateByRebackWorkSummary(PmTrialAssess trialAssessNew);

    PmTrialAssessDTO getByKey(Long id);

    List<PmTrialAssessDTO> listAll(PmTrialAssessQueryCriteria criteria);

    Map<String, Object> listAll(PmTrialAssessQueryCriteria criteria, Pageable pageable);

    void download(List<PmTrialAssessDTO> trialAssessDTOS, HttpServletResponse response) throws IOException;

    PmTrialAssessDTO getByReqCode(String reqCode);

    void writeOaApprovalResult(PmTrialAssess pmTrialAssess);
}
