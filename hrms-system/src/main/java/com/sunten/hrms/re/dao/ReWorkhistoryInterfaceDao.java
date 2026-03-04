package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReWorkhistoryInterface;
import com.sunten.hrms.re.dto.ReWorkhistoryInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 工作经历临时表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReWorkhistoryInterfaceDao extends BaseMapper<ReWorkhistoryInterface> {

    int insertAllColumn(ReWorkhistoryInterface workhistoryInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReWorkhistoryInterface workhistoryInterface);

    int updateAllColumnByKey(ReWorkhistoryInterface workhistoryInterface);

    ReWorkhistoryInterface getByKey(@Param(value = "id") Long id);

    List<ReWorkhistoryInterface> listAllByCriteria(@Param(value = "criteria") ReWorkhistoryInterfaceQueryCriteria criteria);

    List<ReWorkhistoryInterface> listAllByCriteriaPage(@Param(value = "page") Page<ReWorkhistoryInterface> page, @Param(value = "criteria") ReWorkhistoryInterfaceQueryCriteria criteria);
}
