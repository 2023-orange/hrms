package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReTrialApproval;
import com.sunten.hrms.re.dto.ReTrialApprovalQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 试用审批表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-04-25
 */
@Mapper
@Repository
public interface ReTrialApprovalDao extends BaseMapper<ReTrialApproval> {

    int insertAllColumn(ReTrialApproval trialApproval);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReTrialApproval trialApproval);

    int updateAllColumnByKey(ReTrialApproval trialApproval);

    ReTrialApproval getByKey(@Param(value = "id") Long id);

    List<ReTrialApproval> listAllByCriteria(@Param(value = "criteria") ReTrialApprovalQueryCriteria criteria);

    List<ReTrialApproval> listAllByCriteriaPage(@Param(value = "page") Page<ReTrialApproval> page, @Param(value = "criteria") ReTrialApprovalQueryCriteria criteria);
    // 根据OA审批单号获取试用审批单信息
    ReTrialApproval getByReqCode(@Param(value = "reqCode") String reqCode);
    // 审批结束时
    int updateByApprovalEnd(ReTrialApproval trialApproval);
    // 中间节点审批时
    int updateByApprovalUnderwar(ReTrialApproval trialApproval);
    // 撤销试用审批
    int updateByRepealOperation(ReTrialApproval trialApproval);

    void updateTrialAfterPass(ReTrialApproval trialApproval);
}
