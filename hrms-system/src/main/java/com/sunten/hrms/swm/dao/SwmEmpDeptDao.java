package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmEmpDept;
import com.sunten.hrms.swm.dto.SwmEmpDeptQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 薪酬人员管理范围 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-02-24
 */
@Mapper
@Repository
public interface SwmEmpDeptDao extends BaseMapper<SwmEmpDept> {

    int insertAllColumn(SwmEmpDept empDept);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmEmpDept empDept);

    int updateAllColumnByKey(SwmEmpDept empDept);

    SwmEmpDept getByKey(@Param(value = "id") Long id);

    List<SwmEmpDept> listAllByCriteria(@Param(value = "criteria") SwmEmpDeptQueryCriteria criteria);

    List<SwmEmpDept> listAllByCriteriaPage(@Param(value = "page") Page<SwmEmpDept> page, @Param(value = "criteria") SwmEmpDeptQueryCriteria criteria);

    int updateByEnabled(SwmEmpDept empDept);

    int updateEnabledBySeId(SwmEmpDept empDept);

    Boolean checkGenerateByPmType(SwmEmpDept empDept);
}
