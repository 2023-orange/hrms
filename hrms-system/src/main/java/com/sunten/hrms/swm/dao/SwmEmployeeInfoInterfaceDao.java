package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmEmployeeInfoInterface;
import com.sunten.hrms.swm.dto.SwmEmployeeInfoInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 薪酬员工基本信息接口表 Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-03-23
 */
@Mapper
@Repository
public interface SwmEmployeeInfoInterfaceDao extends BaseMapper<SwmEmployeeInfoInterface> {

    int insertAllColumn(SwmEmployeeInfoInterface employeeInfoInterface);

    int deleteByKey(@Param(value = "id") Double id);

    int deleteByEntityKey(SwmEmployeeInfoInterface employeeInfoInterface);

    int updateAllColumnByKey(SwmEmployeeInfoInterface employeeInfoInterface);

    SwmEmployeeInfoInterface getByKey(@Param(value = "id") Double id);

    List<SwmEmployeeInfoInterface> listAllByCriteria(@Param(value = "criteria") SwmEmployeeInfoInterfaceQueryCriteria criteria);

    List<SwmEmployeeInfoInterface> listAllByCriteriaPage(@Param(value = "page") Page<SwmEmployeeInfoInterface> page, @Param(value = "criteria") SwmEmployeeInfoInterfaceQueryCriteria criteria);

    void insertSwmEmployeeInfoInterfaceToMain(Map<String, Object> map);

    void checkSwmEmployeeInfoInterface(Map<String, Object> map);

    List<SwmEmployeeInfoInterface> getSwmEmployeeInfoSummaryByImportList(@Param(value = "workCards") Set<String> workCards, @Param(value = "groupIds")Set<Long> groupIds);
}
