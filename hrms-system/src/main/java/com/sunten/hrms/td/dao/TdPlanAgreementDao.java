package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdPlanAgreement;
import com.sunten.hrms.td.dto.TdPlanAgreementQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训协议书记录表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-06-18
 */
@Mapper
@Repository
public interface TdPlanAgreementDao extends BaseMapper<TdPlanAgreement> {

    int insertAllColumn(TdPlanAgreement planAgreement);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdPlanAgreement planAgreement);

    int updateAllColumnByKey(TdPlanAgreement planAgreement);

    TdPlanAgreement getByKey(@Param(value = "id") Long id);

    List<TdPlanAgreement> listAllByCriteria(@Param(value = "criteria") TdPlanAgreementQueryCriteria criteria);

    List<TdPlanAgreement> listAllByCriteriaPage(@Param(value = "page") Page<TdPlanAgreement> page, @Param(value = "criteria") TdPlanAgreementQueryCriteria criteria);

    TdPlanAgreement getByPlanIdAndEmployeeId(@Param(value = "employeeId") Long employeeId, @Param(value = "planId") Long planId, @Param(value = "checkMethod") String checkMethod);

    void disabledByCheckMethodAndPlanID(@Param(value = "planId")Long planId, @Param(value = "checkMethod")String checkMethod);

}
