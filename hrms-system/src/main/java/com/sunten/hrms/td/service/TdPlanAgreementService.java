package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdPlanAgreement;
import com.sunten.hrms.td.dto.TdPlanAgreementDTO;
import com.sunten.hrms.td.dto.TdPlanAgreementQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训协议书记录表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-06-18
 */
public interface TdPlanAgreementService extends IService<TdPlanAgreement> {

    TdPlanAgreementDTO insert(TdPlanAgreement planAgreementNew);

    void delete(Long id);

    void delete(TdPlanAgreement planAgreement);

    void update(TdPlanAgreement planAgreementNew);

    TdPlanAgreementDTO getByKey(Long id);

    List<TdPlanAgreementDTO> listAll(TdPlanAgreementQueryCriteria criteria);

    Map<String, Object> listAll(TdPlanAgreementQueryCriteria criteria, Pageable pageable);

    void download(List<TdPlanAgreementDTO> planAgreementDTOS, HttpServletResponse response) throws IOException;

    TdPlanAgreementDTO getByPlanIdAndEmployeeId(Long employeeId, Long planId, String checkMethod);

    void disabledByCheckMethodAndPlanID(Long planId, String checkMethod);
}
