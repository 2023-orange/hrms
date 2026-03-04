package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReWorkhistory;
import com.sunten.hrms.re.dto.ReWorkhistoryQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 工作经历表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReWorkhistoryDao extends BaseMapper<ReWorkhistory> {

    int insertAllColumn(ReWorkhistory workhistory);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReWorkhistory workhistory);

    int updateAllColumnByKey(ReWorkhistory workhistory);

    ReWorkhistory getByKey(@Param(value = "id") Long id);

    List<ReWorkhistory> listAllByCriteria(@Param(value = "criteria") ReWorkhistoryQueryCriteria criteria);

    List<ReWorkhistory> listAllByCriteriaPage(@Param(value = "page") Page<ReWorkhistory> page, @Param(value = "criteria") ReWorkhistoryQueryCriteria criteria);

    int updateEnableFlag(ReWorkhistory workhistory);
}
