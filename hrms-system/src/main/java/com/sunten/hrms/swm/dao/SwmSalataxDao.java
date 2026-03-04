package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmSalatax;
import com.sunten.hrms.swm.dto.SwmSalataxQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 旧的所得税表 Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-10
 */
@Mapper
@Repository
public interface SwmSalataxDao extends BaseMapper<SwmSalatax> {

    int insertAllColumn(SwmSalatax salatax);

    int deleteByKey();

    int deleteByEntityKey(SwmSalatax salatax);

    int updateAllColumnByKey(SwmSalatax salatax);

    SwmSalatax getByKey();

    List<SwmSalatax> listAllByCriteria(@Param(value = "criteria") SwmSalataxQueryCriteria criteria);

    List<SwmSalatax> listAllByCriteriaPage(@Param(value = "page") Page<SwmSalatax> page, @Param(value = "criteria") SwmSalataxQueryCriteria criteria);

    // 查看2006奖金所得税明细
    List<SwmSalatax> getOldSalatax(@Param(value = "workCard") String workCard, @Param(value="year") String year);
}
