package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeEducation;
import com.sunten.hrms.pm.domain.PmEmployeeFamily;
import com.sunten.hrms.pm.dto.PmEmployeeFamilyQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 家庭情况表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeFamilyDao extends BaseMapper<PmEmployeeFamily> {

    int insertAllColumn(PmEmployeeFamily employeeFamily);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeFamily employeeFamily);

    int updateAllColumnByKey(PmEmployeeFamily employeeFamily);

    PmEmployeeFamily getByKey(@Param(value = "id") Long id);

    List<PmEmployeeFamily> listAllByCriteria(@Param(value = "criteria") PmEmployeeFamilyQueryCriteria criteria);

    List<PmEmployeeFamily> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeFamily> page, @Param(value = "criteria") PmEmployeeFamilyQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeFamily employeeFamily);

    List<PmEmployeeFamily> listAllAndTempByEmployye(@Param(value = "employeeId") Long employeeId);

    List<PmEmployeeFamily> superQuery(@Param(value = "queryValue")String queryValue);

    List<PmEmployeeFamily> getChild(@Param(value = "employeeId")Long employeeId);
}
