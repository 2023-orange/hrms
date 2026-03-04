package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmPersonalIncomeTaxInterface;
import com.sunten.hrms.swm.dto.SwmPersonalIncomeTaxInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 个人所得税接口表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-01-14
 */
@Mapper
@Repository
public interface SwmPersonalIncomeTaxInterfaceDao extends BaseMapper<SwmPersonalIncomeTaxInterface> {

    int insertAllColumn(SwmPersonalIncomeTaxInterface personalIncomeTaxInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmPersonalIncomeTaxInterface personalIncomeTaxInterface);

    int updateAllColumnByKey(SwmPersonalIncomeTaxInterface personalIncomeTaxInterface);

    SwmPersonalIncomeTaxInterface getByKey(@Param(value = "id") Long id);

    List<SwmPersonalIncomeTaxInterface> listAllByCriteria(@Param(value = "criteria") SwmPersonalIncomeTaxInterfaceQueryCriteria criteria);

    List<SwmPersonalIncomeTaxInterface> listAllByCriteriaPage(@Param(value = "page") Page<SwmPersonalIncomeTaxInterface> page, @Param(value = "criteria") SwmPersonalIncomeTaxInterfaceQueryCriteria criteria);

    int insertByInterface(SwmPersonalIncomeTaxInterface personalIncomeTaxInterface);

    int insertByInterfaceWithNotAmount(SwmPersonalIncomeTaxInterface personalIncomeTaxInterface);

}
