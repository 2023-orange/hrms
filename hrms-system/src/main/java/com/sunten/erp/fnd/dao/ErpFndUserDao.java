package com.sunten.erp.fnd.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunten.config.DataSourceKeyEnum;
import com.sunten.config.annotation.DataSource;
import com.sunten.erp.fnd.domain.ErpFndUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
@DataSource(DataSourceKeyEnum.ERP)
public interface ErpFndUserDao extends BaseMapper<ErpFndUser> {
    List<ErpFndUser> selectFndUserByName(
            @Param(value = "userName") String userName);

    List<ErpFndUser> selectFndUserByNamePaging(
            @Param(value = "userName") String userName,
            @Param(value = "startRow") Integer startRow,
            @Param(value = "endRow") Integer endRow,
            @Param(value = "sort") String sort,
            @Param(value = "order") String order);

    Integer countFndUserByName(@Param(value = "userName") String userName);

    String validateLogin(@Param(value = "userName") String userName,
                         @Param(value = "pass") String pass);
}
