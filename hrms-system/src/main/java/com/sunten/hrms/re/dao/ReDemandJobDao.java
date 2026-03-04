package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReDemandJob;
import com.sunten.hrms.re.dto.QueryUsedByTrialApprovalCriteria;
import com.sunten.hrms.re.dto.ReDemandJobQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.re.vo.UsedByTrialApprovalDemandVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 用人需求岗位子表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-04-23
 */
@Mapper
@Repository
public interface ReDemandJobDao extends BaseMapper<ReDemandJob> {

    int insertAllColumn(ReDemandJob demandJob);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReDemandJob demandJob);

    int updateAllColumnByKey(ReDemandJob demandJob);

    ReDemandJob getByKey(@Param(value = "id") Long id);

    List<ReDemandJob> listAllByCriteria(@Param(value = "criteria") ReDemandJobQueryCriteria criteria);

    List<ReDemandJob> listAllByCriteriaPage(@Param(value = "page") Page<ReDemandJob> page, @Param(value = "criteria") ReDemandJobQueryCriteria criteria);

    List<ReDemandJob> getDemandJobListByDemandId(@Param(value = "id") Long id);

    int disabledByEnabled(@Param(value = "id")Long id);

    int updateColumnByValue(ReDemandJob demandJob);

    // 试用审批试用的查询
    List<UsedByTrialApprovalDemandVo> queryUsedByTrialApproval(@Param(value = "criteria") QueryUsedByTrialApprovalCriteria queryUsedByTrialApproval);
    // 其它关联模块申请单生成时调用
    void updateInUsedQuantityAfterUsed(@Param(value = "id")Long id);
    // 其它关联模块申请单通过时调用
    void updatePassQuantityAfterUsed(@Param(value = "id")Long id);
    // 其它关联模块申请单不通过或取消时调用
    void updateInUsedQuantityAfterCancel(@Param(value = "id")Long id);
    // charge开放用人需求
    void updatePassQuantityByCharge(ReDemandJob reDemandJob);

    Integer checkNeedCountAfterUpdate(@Param(value = "demandId")Long demandId);

    Integer checkResidueQuantityBeforeTrialCommit(@Param(value = "demandJobId")Long demandJobId);

    List<ReDemandJob> getCurrentStatusList(@Param(value = "deptId")Long deptId,
                                           @Param(value = "demandCode")String demandCode);
}
