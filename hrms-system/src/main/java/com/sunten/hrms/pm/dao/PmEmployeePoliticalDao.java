package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeePolitical;
import com.sunten.hrms.pm.dto.PmEmployeePoliticalQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 政治面貌表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeePoliticalDao extends BaseMapper<PmEmployeePolitical> {

    int insertAllColumn(PmEmployeePolitical employeePolitical);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeePolitical employeePolitical);

    int updateAllColumnByKey(PmEmployeePolitical employeePolitical);

    PmEmployeePolitical getByKey(@Param(value = "id") Long id);

    List<PmEmployeePolitical> listAllByCriteria(@Param(value = "criteria") PmEmployeePoliticalQueryCriteria criteria);

    List<PmEmployeePolitical> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeePolitical> page, @Param(value = "criteria") PmEmployeePoliticalQueryCriteria criteria);

    int updateEnableFlag(PmEmployeePolitical employeePolitical);

    List<PmEmployeePolitical> listAllAndTempByEmployee(@Param(value = "employeeId") Long employeeId);

    List<PmEmployeePolitical> superQuery(@Param(value = "queryValue")String queryValue);
}
