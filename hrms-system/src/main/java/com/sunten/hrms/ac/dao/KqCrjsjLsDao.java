package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.KqCrjsjLs;
import com.sunten.hrms.ac.dto.KqCrjsjLsQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
public interface KqCrjsjLsDao extends BaseMapper<KqCrjsjLs> {

    int insertAllColumn(KqCrjsjLs kqCrjsjLs);

    int deleteByKey(@Param(value = "date8") LocalDateTime date8, @Param(value = "kh") String kh, @Param(value = "mjkzqbh") String mjkzqbh, @Param(value = "time8") LocalDateTime time8);

    int deleteByEntityKey(KqCrjsjLs kqCrjsjLs);

    int updateAllColumnByKey(KqCrjsjLs kqCrjsjLs);

    KqCrjsjLs getByKey(@Param(value = "date8") LocalDateTime date8, @Param(value = "kh") String kh, @Param(value = "mjkzqbh") String mjkzqbh, @Param(value = "time8") LocalDateTime time8);

    List<KqCrjsjLs> listAllByCriteria(@Param(value = "criteria") KqCrjsjLsQueryCriteria criteria);

    List<KqCrjsjLs> listAllByCriteriaPage(@Param(value = "page") Page<KqCrjsjLs> page, @Param(value = "criteria") KqCrjsjLsQueryCriteria criteria);
}
