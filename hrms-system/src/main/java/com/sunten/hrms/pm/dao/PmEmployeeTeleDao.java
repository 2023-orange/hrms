package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeTele;
import com.sunten.hrms.pm.dto.PmEmployeeTeleQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 办公电话表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeTeleDao extends BaseMapper<PmEmployeeTele> {

    int insertAllColumn(PmEmployeeTele employeeTele);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeTele employeeTele);

    int updateAllColumnByKey(PmEmployeeTele employeeTele);

    PmEmployeeTele getByKey(@Param(value = "id") Long id);

    List<PmEmployeeTele> listAllByCriteria(@Param(value = "criteria") PmEmployeeTeleQueryCriteria criteria);

    List<PmEmployeeTele> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeTele> page, @Param(value = "criteria") PmEmployeeTeleQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeTele employeeTele);

    List<PmEmployeeTele> superQuery(@Param(value = "queryValue")String queryValue);

    int insertByRecruitment(PmEmployeeTele employeeTele);
}
