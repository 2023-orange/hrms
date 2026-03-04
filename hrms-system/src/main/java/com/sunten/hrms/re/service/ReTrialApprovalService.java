package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReTrialApproval;
import com.sunten.hrms.re.dto.ReTrialApprovalDTO;
import com.sunten.hrms.re.dto.ReTrialApprovalQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 试用审批表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-04-25
 */
public interface ReTrialApprovalService extends IService<ReTrialApproval> {

    ReTrialApprovalDTO insert(ReTrialApproval trialApprovalNew);

    void delete(Long id);

    void delete(ReTrialApproval trialApproval);

    void update(ReTrialApproval trialApprovalNew);

    ReTrialApprovalDTO getByKey(Long id);

    List<ReTrialApprovalDTO> listAll(ReTrialApprovalQueryCriteria criteria);

    Map<String, Object> listAll(ReTrialApprovalQueryCriteria criteria, Pageable pageable);

    void download(List<ReTrialApprovalDTO> trialApprovalDTOS, HttpServletResponse response) throws IOException;

    ReTrialApprovalDTO getByReqCode(String reqCode);
    // 撤销试用审批单
    void repealTrialApproval(ReTrialApproval trialApprovalNew);
    // OA审批结果反馈
    void writeOaApprovalResult(ReTrialApproval trialApproval);

    void updateTrialAfterPass(ReTrialApproval trialApproval);
}
