package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.KqCrjsj;
import com.sunten.hrms.ac.dto.KqCrjsjQueryCriteria;
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
public interface KqCrjsjDao extends BaseMapper<KqCrjsj> {

    int insertAllColumn(KqCrjsj kqCrjsj);

    int deleteByKey();

    int deleteByEntityKey(KqCrjsj kqCrjsj);

    int updateAllColumnByKey(KqCrjsj kqCrjsj);

    KqCrjsj getByKey();

    List<KqCrjsj> listAllByCriteria(@Param(value = "criteria") KqCrjsjQueryCriteria criteria);

    List<KqCrjsj> listAllByCriteriaPage(@Param(value = "page") Page<KqCrjsj> page, @Param(value = "criteria") KqCrjsjQueryCriteria criteria);


}
