package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmSalesApprovalRelations;
import com.sunten.hrms.pm.dto.PmSalesApprovalRelationsQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 销售审批节点关系表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2022-02-17
 */
@Mapper
@Repository
public interface PmSalesApprovalRelationsDao extends BaseMapper<PmSalesApprovalRelations> {

    int insertAllColumn(PmSalesApprovalRelations salesApprovalRelations);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmSalesApprovalRelations salesApprovalRelations);

    int updateAllColumnByKey(PmSalesApprovalRelations salesApprovalRelations);

    PmSalesApprovalRelations getByKey(@Param(value = "id") Long id);

    List<PmSalesApprovalRelations> listAllByCriteria(@Param(value = "criteria") PmSalesApprovalRelationsQueryCriteria criteria);

    List<PmSalesApprovalRelations> listAllByCriteriaPage(@Param(value = "page") Page<PmSalesApprovalRelations> page, @Param(value = "criteria") PmSalesApprovalRelationsQueryCriteria criteria);
}
