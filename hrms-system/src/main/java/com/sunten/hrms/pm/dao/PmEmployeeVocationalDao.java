package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeTele;
import com.sunten.hrms.pm.domain.PmEmployeeVocational;
import com.sunten.hrms.pm.dto.PmEmployeeVocationalQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 职业资格表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeVocationalDao extends BaseMapper<PmEmployeeVocational> {

    int insertAllColumn(PmEmployeeVocational employeeVocational);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeVocational employeeVocational);

    int updateAllColumnByKey(PmEmployeeVocational employeeVocational);

    PmEmployeeVocational getByKey(@Param(value = "id") Long id);

    PmEmployeeVocational getMainVocationalByKey(@Param(value = "employeeId") Long employeeId);

    List<PmEmployeeVocational> listAllByCriteria(@Param(value = "criteria") PmEmployeeVocationalQueryCriteria criteria);

    List<PmEmployeeVocational> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeVocational> page, @Param(value = "criteria") PmEmployeeVocationalQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeVocational employeeVocational);

    List<PmEmployeeVocational> listAllAndTempByEmployee(@Param(value = "employeeId") Long employeeId);

    List<PmEmployeeVocational> superQuery(@Param(value = "queryValue")String queryValue);
}
