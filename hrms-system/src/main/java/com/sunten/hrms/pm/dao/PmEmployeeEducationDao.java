package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeEducation;
import com.sunten.hrms.pm.dto.PmEmployeeEducationQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 教育信息表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeEducationDao extends BaseMapper<PmEmployeeEducation> {

    int insertAllColumn(PmEmployeeEducation employeeEducation);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeEducation employeeEducation);

    int updateAllColumnByKey(PmEmployeeEducation employeeEducation);

    PmEmployeeEducation getByKey(@Param(value = "id") Long id);

    PmEmployeeEducation getMainEducationByKey(@Param(value = "employeeId") Long employeeId);

    List<PmEmployeeEducation> listAllByCriteria(@Param(value = "criteria") PmEmployeeEducationQueryCriteria criteria);

    List<PmEmployeeEducation> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeEducation> page, @Param(value = "criteria") PmEmployeeEducationQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeEducation employeeEducation);

    int updateNewEducationFlag(PmEmployeeEducation employeeEducation);

    List<PmEmployeeEducation> listAllAndTempByEmployee(@Param(value = "employeeId") Long employeeId);

    List<PmEmployeeEducation> superQuery(@Param(value = "queryValue")String queryValue);
}
