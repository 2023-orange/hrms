package com.sunten.hrms.fnd.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.fnd.domain.FndDict;
import com.sunten.hrms.fnd.dto.FndDictQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Mapper
@Repository
public interface FndDictDao extends BaseMapper<FndDict> {

    int insertAllColumn(FndDict dict);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndDict dict);

    int updateAllColumnByKey(FndDict dict);

    FndDict getByKey(@Param(value = "id") Long id);

    List<FndDict> listAllByCriteria(@Param(value = "criteria") FndDictQueryCriteria criteria);

    List<FndDict> listAllByCriteriaPage(@Param(value = "page") Page<FndDict> page, @Param(value = "criteria") FndDictQueryCriteria criteria);
}
