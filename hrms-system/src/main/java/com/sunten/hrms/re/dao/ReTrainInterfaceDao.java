package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReTrainInterface;
import com.sunten.hrms.re.dto.ReTrainInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训记录临时表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReTrainInterfaceDao extends BaseMapper<ReTrainInterface> {

    int insertAllColumn(ReTrainInterface trainInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReTrainInterface trainInterface);

    int updateAllColumnByKey(ReTrainInterface trainInterface);

    ReTrainInterface getByKey(@Param(value = "id") Long id);

    List<ReTrainInterface> listAllByCriteria(@Param(value = "criteria") ReTrainInterfaceQueryCriteria criteria);

    List<ReTrainInterface> listAllByCriteriaPage(@Param(value = "page") Page<ReTrainInterface> page, @Param(value = "criteria") ReTrainInterfaceQueryCriteria criteria);
}
