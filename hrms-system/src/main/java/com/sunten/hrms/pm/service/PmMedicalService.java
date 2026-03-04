package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmMedical;
import com.sunten.hrms.pm.dto.PmMedicalDTO;
import com.sunten.hrms.pm.dto.PmMedicalQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 体检申请表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-04-07
 */
public interface PmMedicalService extends IService<PmMedical> {

    PmMedicalDTO insert(PmMedical medicalNew);

    void delete(Long id);

    void delete(PmMedical medical);

    void update(PmMedical medicalNew);

    PmMedicalDTO getByKey(Long id);

    List<PmMedicalDTO> listAll(PmMedicalQueryCriteria criteria);

    Map<String, Object> listAll(PmMedicalQueryCriteria criteria, Pageable pageable);

    void download(List<PmMedicalDTO> medicalDTOS, HttpServletResponse response) throws IOException;
    // 根据OA单号获取申请单明细
    PmMedicalDTO getByReqCode(String reqCode);
    // 修改体检申请单审批内容
    void updateApprovalContent(String reqCode,String approvalNode, String approvalEmployee, String approvalResult);

    int getPmMedicalPass();
}
