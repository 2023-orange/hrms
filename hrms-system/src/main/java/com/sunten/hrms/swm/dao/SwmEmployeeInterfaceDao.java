package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmEmployeeInterface;
import com.sunten.hrms.swm.dto.SwmEmployeeInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 薪酬员工信息接口表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-09-13
 */
@Mapper
@Repository
public interface SwmEmployeeInterfaceDao extends BaseMapper<SwmEmployeeInterface> {

    int insertAllColumn(SwmEmployeeInterface employeeInterface);

    int deleteByKey(@Param(value = "id") Double id);

    int deleteByEntityKey(SwmEmployeeInterface employeeInterface);

    int updateAllColumnByKey(SwmEmployeeInterface employeeInterface);

    SwmEmployeeInterface getByKey(@Param(value = "id") Double id);

    List<SwmEmployeeInterface> listAllByCriteria(@Param(value = "criteria") SwmEmployeeInterfaceQueryCriteria criteria);

    List<SwmEmployeeInterface> listAllByCriteriaPage(@Param(value = "page") Page<SwmEmployeeInterface> page, @Param(value = "criteria") SwmEmployeeInterfaceQueryCriteria criteria);

    int insertSwmEmployeeInterface(SwmEmployeeInterface swmEmployeeInterface);


    List<SwmEmployeeInterface> getSwmEmployeeSummaryByImportList(@Param(value = "workCards") Set<String> workCards, @Param(value = "groupIds")Set<Long> groupIds);

    void interfaceToHistory(@Param(value = "groupId")Long groupId);
}
