package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmPermissionLeaveApproval;
import com.sunten.hrms.pm.dto.PmPermissionLeaveApprovalQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 离职申请与IT权限关联表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-05-10
 */
@Mapper
@Repository
public interface PmPermissionLeaveApprovalDao extends BaseMapper<PmPermissionLeaveApproval> {

    int insertAllColumn(PmPermissionLeaveApproval permissionLeaveApproval);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmPermissionLeaveApproval permissionLeaveApproval);

    int updateAllColumnByKey(PmPermissionLeaveApproval permissionLeaveApproval);

    PmPermissionLeaveApproval getByKey(@Param(value = "id") Long id);

    List<PmPermissionLeaveApproval> listAllByCriteria(@Param(value = "criteria") PmPermissionLeaveApprovalQueryCriteria criteria);

    List<PmPermissionLeaveApproval> listAllByCriteriaPage(@Param(value = "page") Page<PmPermissionLeaveApproval> page, @Param(value = "criteria") PmPermissionLeaveApprovalQueryCriteria criteria);
}
