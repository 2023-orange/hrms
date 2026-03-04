package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeRehire;
import com.sunten.hrms.pm.dto.PmEmployeeRehireQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 返聘协议表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeRehireDao extends BaseMapper<PmEmployeeRehire> {

    int insertAllColumn(PmEmployeeRehire employeeRehire);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeRehire employeeRehire);

    int updateAllColumnByKey(PmEmployeeRehire employeeRehire);

    PmEmployeeRehire getByKey(@Param(value = "id") Long id);

    List<PmEmployeeRehire> listAllByCriteria(@Param(value = "criteria") PmEmployeeRehireQueryCriteria criteria);

    List<PmEmployeeRehire> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeRehire> page, @Param(value = "criteria") PmEmployeeRehireQueryCriteria criteria);
}
