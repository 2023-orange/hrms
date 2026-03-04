package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndDynamicQueryGroupDetail;
import com.sunten.hrms.fnd.dto.FndDynamicQueryGroupDetailQueryCriteria;
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
 * @author batan
 * @since 2022-07-29
 */
@Mapper
@Repository
public interface FndDynamicQueryGroupDetailDao extends BaseMapper<FndDynamicQueryGroupDetail> {

    int insertAllColumn(FndDynamicQueryGroupDetail dynamicQueryGroupDetail);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndDynamicQueryGroupDetail dynamicQueryGroupDetail);

    int deleteByGroupId(@Param(value = "groupId") Long groupId);

    int updateAllColumnByKey(FndDynamicQueryGroupDetail dynamicQueryGroupDetail);

    FndDynamicQueryGroupDetail getByKey(@Param(value = "id") Long id);

    List<FndDynamicQueryGroupDetail> listAllByCriteria(@Param(value = "criteria") FndDynamicQueryGroupDetailQueryCriteria criteria);

    List<FndDynamicQueryGroupDetail> listAllByCriteriaPage(@Param(value = "page") Page<FndDynamicQueryGroupDetail> page, @Param(value = "criteria") FndDynamicQueryGroupDetailQueryCriteria criteria);
}
