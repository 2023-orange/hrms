package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReFamily;
import com.sunten.hrms.re.dto.ReFamilyQueryCriteria;
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
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReFamilyDao extends BaseMapper<ReFamily> {

    int insertAllColumn(ReFamily family);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReFamily family);

    int updateAllColumnByKey(ReFamily family);

    ReFamily getByKey(@Param(value = "id") Long id);

    List<ReFamily> listAllByCriteria(@Param(value = "criteria") ReFamilyQueryCriteria criteria);

    List<ReFamily> listAllByCriteriaPage(@Param(value = "page") Page<ReFamily> page, @Param(value = "criteria") ReFamilyQueryCriteria criteria);

    int updateEnableFlag(ReFamily family);
}
