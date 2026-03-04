package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdPlanResult;
import com.sunten.hrms.td.dto.TdPlanResultQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.td.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训结果记录 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-06-16
 */
@Mapper
@Repository
public interface TdPlanResultDao extends BaseMapper<TdPlanResult> {

    int insertAllColumn(TdPlanResult planResult);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdPlanResult planResult);

    int updateAllColumnByKey(TdPlanResult planResult);

    TdPlanResult getByKey(@Param(value = "id") Long id);

    List<TdPlanResult> listAllByCriteria(@Param(value = "criteria") TdPlanResultQueryCriteria criteria);

    List<TdPlanResult> listAllByCriteriaPage(@Param(value = "page") Page<TdPlanResult> page, @Param(value = "criteria") TdPlanResultQueryCriteria criteria);

    int interfaceToMain(@Param(value = "groupId")Long groupId);

    int disabledByPlanId(TdPlanResult tdPlanResult);
    // 部门培训项目统计
    List<DeptPlanExcelVo> listDeptPlanExcelVo(DeptPlanExcelVo deptPlanExcelVo);
    // 项目培训计划统计
    List<PmPlanExcelVo> listEmpByPlanExcelVo(PmPlanExcelVo pmPlanExcelVo);
    // 员工培训统计
    List<PmPlanHistoryVo> listEmpPlanHistory(PmPlanHistoryVo pmPlanHistoryVo);
    // 培训协议书导出
    List<AgreementExcelVo> listAgreementForExcel(AgreementExcelVo agreementExcelVo);
    // 子集使用的查询接口
    List<PlanChildResultVo> listForChild(@Param(value = "employeeId")Long employeeId);

    List<PlanChildResultVo> superQuery(@Param(value = "queryValue")String queryValue);

}
