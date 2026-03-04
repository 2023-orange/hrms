package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeAwardInterface;
import com.sunten.hrms.pm.dto.PmEmployeeAwardInterfaceQueryCriteria;
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
 * @author xk
 * @since 2021-09-23
 */
@Mapper
@Repository
public interface PmEmployeeAwardInterfaceDao extends BaseMapper<PmEmployeeAwardInterface> {

    int insertAllColumn(PmEmployeeAwardInterface employeeAwardInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeAwardInterface employeeAwardInterface);

    int updateAllColumnByKey(PmEmployeeAwardInterface employeeAwardInterface);

    PmEmployeeAwardInterface getByKey(@Param(value = "id") Long id);

    List<PmEmployeeAwardInterface> listAllByCriteria(@Param(value = "criteria") PmEmployeeAwardInterfaceQueryCriteria criteria);

    List<PmEmployeeAwardInterface> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeAwardInterface> page, @Param(value = "criteria") PmEmployeeAwardInterfaceQueryCriteria criteria);

    int insertToInterface(PmEmployeeAwardInterface employeeAwardInterface);
}
