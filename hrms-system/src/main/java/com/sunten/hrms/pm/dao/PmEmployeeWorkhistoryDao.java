package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeWorkhistory;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 工作经历表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeWorkhistoryDao extends BaseMapper<PmEmployeeWorkhistory> {

    int insertAllColumn(PmEmployeeWorkhistory employeeWorkhistory);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeWorkhistory employeeWorkhistory);

    int updateAllColumnByKey(PmEmployeeWorkhistory employeeWorkhistory);

    PmEmployeeWorkhistory getByKey(@Param(value = "id") Long id);

    List<PmEmployeeWorkhistory> listAllByCriteria(@Param(value = "criteria") PmEmployeeWorkhistoryQueryCriteria criteria);

    List<PmEmployeeWorkhistory> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeWorkhistory> page, @Param(value = "criteria") PmEmployeeWorkhistoryQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeWorkhistory employeeWorkhistory);

    List<PmEmployeeWorkhistory> listAllAndTempByEmployee(@Param(value = "employeeId") Long employeeId);

    List<PmEmployeeWorkhistory> superQuery(@Param(value = "queryValue")String queryValue);
}
