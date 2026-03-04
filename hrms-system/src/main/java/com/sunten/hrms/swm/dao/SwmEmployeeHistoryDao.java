package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmEmployeeHistory;
import com.sunten.hrms.swm.dto.SwmEmployeeHistoryQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 员工信息历史表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmEmployeeHistoryDao extends BaseMapper<SwmEmployeeHistory> {

    int insertAllColumn(SwmEmployeeHistory employeeHistory);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmEmployeeHistory employeeHistory);

    int updateAllColumnByKey(SwmEmployeeHistory employeeHistory);

    SwmEmployeeHistory getByKey(@Param(value = "id") Long id);

    List<SwmEmployeeHistory> listAllByCriteria(@Param(value = "criteria") SwmEmployeeHistoryQueryCriteria criteria);

    List<SwmEmployeeHistory> listAllByCriteriaPage(@Param(value = "page") Page<SwmEmployeeHistory> page, @Param(value = "criteria") SwmEmployeeHistoryQueryCriteria criteria);

    List<SwmEmployeeHistory> getSwmEmployeeHistoryByWorkCard(@Param(value = "workCard") String workCard);
}
