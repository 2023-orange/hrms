package com.sunten.erp.cux.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunten.config.DataSourceKeyEnum;
import com.sunten.config.annotation.DataSource;
import com.sunten.erp.cux.domain.CuxTestTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Mapper
@Repository
@DataSource(DataSourceKeyEnum.ERP)
public interface CuxTestTransactionDao extends BaseMapper<CuxTestTransaction> {

    int insertAllColumn(CuxTestTransaction cuxTestTransaction);

    List<CuxTestTransaction> listAll();
}
