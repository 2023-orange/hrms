package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmEmployeeMonthlyBak;
import com.sunten.hrms.swm.dto.SwmEmployeeMonthlyBakQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 薪酬员工信息每月备份表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-09-15
 */
@Mapper
@Repository
public interface SwmEmployeeMonthlyBakDao extends BaseMapper<SwmEmployeeMonthlyBak> {

    int insertAllColumn(SwmEmployeeMonthlyBak employeeMonthlyBak);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmEmployeeMonthlyBak employeeMonthlyBak);

    int updateAllColumnByKey(SwmEmployeeMonthlyBak employeeMonthlyBak);

    SwmEmployeeMonthlyBak getByKey(@Param(value = "id") Long id);

    List<SwmEmployeeMonthlyBak> listAllByCriteria(@Param(value = "criteria") SwmEmployeeMonthlyBakQueryCriteria criteria);

    List<SwmEmployeeMonthlyBak> listAllByCriteriaPage(@Param(value = "page") Page<SwmEmployeeMonthlyBak> page, @Param(value = "criteria") SwmEmployeeMonthlyBakQueryCriteria criteria);

    int autoInsertBySwmEmployeeMonthly(@Param(value = "incomePeriod")String incomePeriod);

    Boolean checkBeforeMonthlyJob();

    int deleteMonthlyBak(@Param(value = "incomePeriod")String incomePeriod);
}
