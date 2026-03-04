package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmConsolationMoneyInterface;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 慰问金接口表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-08-05
 */
@Mapper
@Repository
public interface SwmConsolationMoneyInterfaceDao extends BaseMapper<SwmConsolationMoneyInterface> {

    int insertAllColumn(SwmConsolationMoneyInterface consolationMoneyInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmConsolationMoneyInterface consolationMoneyInterface);

    int updateAllColumnByKey(SwmConsolationMoneyInterface consolationMoneyInterface);

    SwmConsolationMoneyInterface getByKey(@Param(value = "id") Long id);

    List<SwmConsolationMoneyInterface> listAllByCriteria(@Param(value = "criteria") SwmConsolationMoneyInterfaceQueryCriteria criteria);

    List<SwmConsolationMoneyInterface> listAllByCriteriaPage(@Param(value = "page") Page<SwmConsolationMoneyInterface> page, @Param(value = "criteria") SwmConsolationMoneyInterfaceQueryCriteria criteria);

    int importOldestConsolationMoney(SwmConsolationMoneyInterface consolationMoneyInterface);

    int importReleasedConsolationMoney(SwmConsolationMoneyInterface consolationMoneyInterface);
}
