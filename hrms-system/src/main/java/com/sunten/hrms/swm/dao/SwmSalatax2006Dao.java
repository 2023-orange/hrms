package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmSalatax2006;
import com.sunten.hrms.swm.dto.SwmSalatax2006QueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 年底奖金所得税 Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-11
 */
@Mapper
@Repository
public interface SwmSalatax2006Dao extends BaseMapper<SwmSalatax2006> {

    int insertAllColumn(SwmSalatax2006 salatax2006);

    int deleteByKey();

    int deleteByEntityKey(SwmSalatax2006 salatax2006);

    int updateAllColumnByKey(SwmSalatax2006 salatax2006);

    SwmSalatax2006 getByKey();

    List<SwmSalatax2006> listAllByCriteria(@Param(value = "criteria") SwmSalatax2006QueryCriteria criteria);

    List<SwmSalatax2006> listAllByCriteriaPage(@Param(value = "page") Page<SwmSalatax2006> page, @Param(value = "criteria") SwmSalatax2006QueryCriteria criteria);

    // 查看2006奖金所得税明细
    List<SwmSalatax2006> getOldSalatax2006(@Param(value = "workCard") String workCard);
}
