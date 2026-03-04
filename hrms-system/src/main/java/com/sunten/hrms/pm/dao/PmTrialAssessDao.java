package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmTrialAssess;
import com.sunten.hrms.pm.dto.PmTrialAssessQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 新晋员工试用期考核表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-05-07
 */
@Mapper
@Repository
public interface PmTrialAssessDao extends BaseMapper<PmTrialAssess> {

    int insertAllColumn(PmTrialAssess trialAssess);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmTrialAssess trialAssess);

    int updateAllColumnByKey(PmTrialAssess trialAssess);

    PmTrialAssess getByKey(@Param(value = "id") Long id);

    List<PmTrialAssess> listAllByCriteria(@Param(value = "criteria") PmTrialAssessQueryCriteria criteria);

    List<PmTrialAssess> listAllByCriteriaPage(@Param(value = "page") Page<PmTrialAssess> page, @Param(value = "criteria") PmTrialAssessQueryCriteria criteria);
    // 根据OA申请单号获取数据
    PmTrialAssess getByReqCode(@Param(value = "reqCode") String reqCode);
    // 审批结束时
    int updateByApprovalEnd(PmTrialAssess trialAssess);
    // 中间节点审批时
    int updateByApprovalUnderwar(PmTrialAssess trialAssess);

    // 员工本人提交工作总结
    int updateBySubmitWorkSummary(PmTrialAssess trialAssess);

    // 领导打回工作总结
    int updateByRebackWorkSummary(PmTrialAssess trialAssess);
    // 查询员工的试用期考核申请
    List<PmTrialAssess> listByEmployeeId(@Param(value = "employeeId") Long employeeId);
}
