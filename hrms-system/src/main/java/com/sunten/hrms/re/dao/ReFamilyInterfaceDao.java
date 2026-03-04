package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReFamilyInterface;
import com.sunten.hrms.re.dto.ReFamilyInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 家庭情况临时表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReFamilyInterfaceDao extends BaseMapper<ReFamilyInterface> {

    int insertAllColumn(ReFamilyInterface familyInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReFamilyInterface familyInterface);

    int updateAllColumnByKey(ReFamilyInterface familyInterface);

    ReFamilyInterface getByKey(@Param(value = "id") Long id);

    List<ReFamilyInterface> listAllByCriteria(@Param(value = "criteria") ReFamilyInterfaceQueryCriteria criteria);

    List<ReFamilyInterface> listAllByCriteriaPage(@Param(value = "page") Page<ReFamilyInterface> page, @Param(value = "criteria") ReFamilyInterfaceQueryCriteria criteria);
}
