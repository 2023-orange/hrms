package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeJobTransfer;
import com.sunten.hrms.pm.domain.PmTransferRequest;
import com.sunten.hrms.pm.dto.PmTransferRequestQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.swm.domain.SwmEmployee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 岗位调动申请表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-05-24
 */
@Mapper
@Repository
public interface PmTransferRequestDao extends BaseMapper<PmTransferRequest> {

    int insertAllColumn(PmTransferRequest transferRequest);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmTransferRequest transferRequest);

    int updateAllColumnByKey(PmTransferRequest transferRequest);

    PmTransferRequest getByKey(@Param(value = "id") Long id);

    List<PmTransferRequest> listAllByCriteria(@Param(value = "criteria") PmTransferRequestQueryCriteria criteria);

    List<PmTransferRequest> listAllByCriteriaPage(@Param(value = "page") Page<PmTransferRequest> page, @Param(value = "criteria") PmTransferRequestQueryCriteria criteria);
    // 根据OA申请单号获取信息
    PmTransferRequest getByReqCode(@Param(value = "reqCode") String reqCode);
    // 个人意见填写
    int updateBySelfVerify(PmTransferRequest transferRequest);
    // 根据调动编号获取
    PmTransferRequest getByTransferCode(@Param(value = "transferCode") String transferCode);
    // 审批结束时
    int updateByApprovalEnd(PmTransferRequest transferRequest);
    // 中间节点审批时
    int updateByApprovalUnderwar(PmTransferRequest transferRequest);

    int updateAttribute5(PmEmployeeJobTransfer PmEmployeeJobTransfer);

    int updateSwmChangeFlag(SwmEmployee employee);

    int updateTeacherContractFlag(PmTransferRequest transferRequest);

    int updateSubmitFlag(Long id);

    PmTransferRequest getByPmEmployeeId(@Param(value = "pmEmployeeId") String pmEmployeeId);
}
