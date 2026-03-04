package com.sunten.erp.fnd.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.config.DataSourceKeyEnum;
import com.sunten.config.annotation.DataSource;
import com.sunten.erp.fnd.domain.TbaTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
@DataSource(DataSourceKeyEnum.ERP)
public interface TbaTestDao extends BaseMapper<TbaTest> {
    int insertAllColumn(TbaTest tbaTest);

    List<TbaTest> listAllPage(@Param(value = "page") Page<TbaTest> page);
}
