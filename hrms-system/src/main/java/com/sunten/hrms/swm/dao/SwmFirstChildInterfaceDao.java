package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmFirstChildInterface;
import com.sunten.hrms.swm.dto.SwmFirstChildInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 第一胎子女信息登记表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-08-10
 */
@Mapper
@Repository
public interface SwmFirstChildInterfaceDao extends BaseMapper<SwmFirstChildInterface> {

    int insertAllColumn(SwmFirstChildInterface firstChildInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmFirstChildInterface firstChildInterface);

    int updateAllColumnByKey(SwmFirstChildInterface firstChildInterface);

    SwmFirstChildInterface getByKey(@Param(value = "id") Long id);

    List<SwmFirstChildInterface> listAllByCriteria(@Param(value = "criteria") SwmFirstChildInterfaceQueryCriteria criteria);

    List<SwmFirstChildInterface> listAllByCriteriaPage(@Param(value = "page") Page<SwmFirstChildInterface> page, @Param(value = "criteria") SwmFirstChildInterfaceQueryCriteria criteria);

    int insertFirstChildByExcel(SwmFirstChildInterface swmFirstChildInterface);

}
