package com.sunten.hrms.fnd.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.fnd.domain.FndSuperQueryGroup;
import com.sunten.hrms.fnd.domain.FndSuperQueryTemp;
import com.sunten.hrms.fnd.dto.FndSuperQueryTempQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 超级查询数据临时表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-08-19
 */
@Mapper
@Repository
public interface FndSuperQueryTempDao extends BaseMapper<FndSuperQueryTemp> {

    int insertAllColumn(FndSuperQueryTemp superQueryTemp);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndSuperQueryTemp superQueryTemp);

    int updateAllColumnByKey(FndSuperQueryTemp superQueryTemp);

    FndSuperQueryTemp getByKey(@Param(value = "id") Long id);

    List<FndSuperQueryTemp> listAllByCriteria(@Param(value = "criteria") FndSuperQueryTempQueryCriteria criteria);

    List<FndSuperQueryTemp> listAllByCriteriaPage(@Param(value = "page") Page<FndSuperQueryTemp> page, @Param(value = "criteria") FndSuperQueryTempQueryCriteria criteria);

    void insertCollection(List<FndSuperQueryTemp> queryTempList);

    void truncateTemp();

    void deleteTemp(@Param(value = "searchUserId") Long searchUserId);

    void insertTempByUnpivot(@Param(value = "criteria") FndSuperQueryTempQueryCriteria criteria,
                             @Param(value = "group") FndSuperQueryGroup group);

    void insertTempByCross(@Param(value = "criteria") FndSuperQueryTempQueryCriteria criteria,
                           @Param(value = "group") FndSuperQueryGroup group);


}
