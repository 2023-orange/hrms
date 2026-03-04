package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeHobby;
import com.sunten.hrms.pm.dto.PmEmployeeHobbyQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 技术爱好表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeHobbyDao extends BaseMapper<PmEmployeeHobby> {

    int insertAllColumn(PmEmployeeHobby employeeHobby);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeHobby employeeHobby);

    int updateAllColumnByKey(PmEmployeeHobby employeeHobby);

    PmEmployeeHobby getByKey(@Param(value = "id") Long id);

    List<PmEmployeeHobby> listAllByCriteria(@Param(value = "criteria") PmEmployeeHobbyQueryCriteria criteria);

    List<PmEmployeeHobby> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeHobby> page, @Param(value = "criteria") PmEmployeeHobbyQueryCriteria criteria);

    List<PmEmployeeHobby> listAllBySummary(@Param(value = "criteria") PmEmployeeHobbyQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeHobby employeeHobby);

    List<PmEmployeeHobby> listAllAndTempByEmployee(@Param(value = "employeeId") Long employeeId);

    List<PmEmployeeHobby> superQuery(@Param(value = "queryValue")String queryValue);
}
