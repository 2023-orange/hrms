package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmLeaveApproval;
import com.sunten.hrms.pm.dto.PmLeaveApprovalQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.pm.vo.SalesAreaEtcVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 离职审批表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-05-07
 */
@Mapper
@Repository
public interface PmLeaveApprovalDao extends BaseMapper<PmLeaveApproval> {

    int insertAllColumn(PmLeaveApproval leaveApproval);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmLeaveApproval leaveApproval);

    int updateAllColumnByKey(PmLeaveApproval leaveApproval);

    PmLeaveApproval getByKey(@Param(value = "id") Long id);

    List<PmLeaveApproval> listAllByCriteria(@Param(value = "criteria") PmLeaveApprovalQueryCriteria criteria);

    List<PmLeaveApproval> listAllByCriteriaPage(@Param(value = "page") Page<PmLeaveApproval> page, @Param(value = "criteria") PmLeaveApprovalQueryCriteria criteria);

    int disabledById(@Param(value = "id") Long id);

    PmLeaveApproval getByOaOrder(@Param(value = "oaOrder") String oaOrder);

    SalesAreaEtcVo getSalesAreaEtcByDeptId(@Param(value = "deptId")Long deptId);

    Boolean checkIsExistLeaveInApproval(@Param(value = "employeeId")Long employeeId);

    List<PmLeaveApproval> listALLByStatus(@Param(value = "approvalStatus") String approvalStatus);

    Integer countLeaveApproval(@Param(value = "approvalStatus") String approvalStatus);
}
