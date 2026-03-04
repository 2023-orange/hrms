package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.KqCrjsjHis;
import com.sunten.hrms.ac.dto.KqCrjsjHisQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-10-19
 */
@Mapper
@Repository
public interface KqCrjsjHisDao extends BaseMapper<KqCrjsjHis> {

    int insertAllColumn(KqCrjsjHis kqCrjsjHis);

    int deleteByKey();

    int deleteByEntityKey(KqCrjsjHis kqCrjsjHis);

    int updateAllColumnByKey(KqCrjsjHis kqCrjsjHis);

    KqCrjsjHis getByKey();

    List<KqCrjsjHis> listAllByCriteria(@Param(value = "criteria") KqCrjsjHisQueryCriteria criteria);

    List<KqCrjsjHis> listAllByCriteriaPage(@Param(value = "page") Page<KqCrjsjHis> page, @Param(value = "criteria") KqCrjsjHisQueryCriteria criteria);
}
