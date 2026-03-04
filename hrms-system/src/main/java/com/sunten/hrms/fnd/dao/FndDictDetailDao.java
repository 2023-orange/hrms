package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndDictDetail;
import com.sunten.hrms.fnd.dto.FndDictDetailQueryCriteria;
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
 * @since 2019-12-19
 */
@Mapper
@Repository
public interface FndDictDetailDao extends BaseMapper<FndDictDetail> {

    int insertAllColumn(FndDictDetail dictDetail);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndDictDetail dictDetail);

    int updateAllColumnByKey(FndDictDetail dictDetail);

    FndDictDetail getByKey(@Param(value = "id") Long id);

    List<FndDictDetail> listAllByCriteria(@Param(value = "criteria") FndDictDetailQueryCriteria criteria);

    List<FndDictDetail> listAllByCriteriaPage(@Param(value = "page") Page<FndDictDetail> page, @Param(value = "criteria") FndDictDetailQueryCriteria criteria);

    FndDictDetail selectDetailByNameAndLabel(@Param(value = "dictName") String dictName, @Param(value = "label") String label);
}
