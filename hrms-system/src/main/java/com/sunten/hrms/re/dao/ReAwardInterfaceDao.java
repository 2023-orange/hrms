package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReAwardInterface;
import com.sunten.hrms.re.dto.ReAwardInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 奖罚情况临时表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReAwardInterfaceDao extends BaseMapper<ReAwardInterface> {

    int insertAllColumn(ReAwardInterface awardInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReAwardInterface awardInterface);

    int updateAllColumnByKey(ReAwardInterface awardInterface);

    ReAwardInterface getByKey(@Param(value = "id") Long id);

    List<ReAwardInterface> listAllByCriteria(@Param(value = "criteria") ReAwardInterfaceQueryCriteria criteria);

    List<ReAwardInterface> listAllByCriteriaPage(@Param(value = "page") Page<ReAwardInterface> page, @Param(value = "criteria") ReAwardInterfaceQueryCriteria criteria);
}
