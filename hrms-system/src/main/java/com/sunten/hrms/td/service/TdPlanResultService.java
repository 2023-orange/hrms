package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdPlanResult;
import com.sunten.hrms.td.dto.TdPlanResultDTO;
import com.sunten.hrms.td.dto.TdPlanResultQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.td.vo.*;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训结果记录 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-06-16
 */
public interface TdPlanResultService extends IService<TdPlanResult> {

    TdPlanResultDTO insert(TdPlanResult planResultNew);

    void delete(Long id);

    void delete(TdPlanResult planResult);

    void update(TdPlanResult planResultNew);

    TdPlanResultDTO getByKey(Long id);

    List<TdPlanResultDTO> listAll(TdPlanResultQueryCriteria criteria);

    Map<String, Object> listAll(TdPlanResultQueryCriteria criteria, Pageable pageable);

    void download(List<TdPlanResultDTO> planResultDTOS, HttpServletResponse response) throws IOException;

    void interfaceToMain(Long groupId);

    void removeByPlanId(TdPlanResult planResult);
    // 部门培训计划统计
    void downloadDeptPlanExcel(List<DeptPlanExcelVo> deptPlanExcelVos, HttpServletResponse response) throws  IOException;
    // 培训计划情况统计
    void downloadPmPlanExcel(List<PmPlanExcelVo> pmPlanExcelVos, String inTeacher ,HttpServletResponse response) throws IOException;
    // 员工培训统计
    void downloadPmPlanHistoryExcel(List<PmPlanHistoryVo> pmPlanHistoryVos, HttpServletResponse response) throws  IOException;
    // 协议书统计
    void downloadAgreementExcel(List<AgreementExcelVo> agreementExcelVos, HttpServletResponse response) throws  IOException;

    List<DeptPlanExcelVo> getDeptPlanExcelVoList(DeptPlanExcelVo deptPlanExcelVo);

    List<PmPlanExcelVo> listEmpByPlanExcelVo(PmPlanExcelVo pmPlanExcelVo);

    List<PmPlanHistoryVo> listPmPlanHistoryExcelVo(PmPlanHistoryVo pmPlanHistoryVo);

    List<AgreementExcelVo> listAgreementForExcelVo(AgreementExcelVo agreementExcelVo);

    List<PlanChildResultVo> listForChild(Long employeeId);
}
