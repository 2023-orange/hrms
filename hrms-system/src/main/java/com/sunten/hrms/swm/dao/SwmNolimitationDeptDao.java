package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmMonthlyQuarterlyAssessmentNum;
import com.sunten.hrms.swm.domain.SwmNolimitationDept;
import com.sunten.hrms.swm.dto.SwmNolimitationDeptQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-21
 */
@Mapper
@Repository
public interface SwmNolimitationDeptDao extends BaseMapper<SwmNolimitationDept> {

    int insertAllColumn(SwmNolimitationDept nolimitationDept);

    int deleteByKey();

    int deleteByEntityKey(SwmNolimitationDept nolimitationDept);

    int updateAllColumnByKey(SwmNolimitationDept nolimitationDept);

    SwmNolimitationDept getByKey();

    List<SwmNolimitationDept> listAllByCriteria(@Param(value = "criteria") SwmNolimitationDeptQueryCriteria criteria);

    List<SwmNolimitationDept> listAllByCriteriaPage(@Param(value = "page") Page<SwmNolimitationDept> page, @Param(value = "criteria") SwmNolimitationDeptQueryCriteria criteria);

    Long countDept(String deptName);

    List<SwmNolimitationDept> getSwmDept();
}
