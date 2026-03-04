package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReAward;
import com.sunten.hrms.re.dto.ReAwardQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 奖罚情况表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReAwardDao extends BaseMapper<ReAward> {

    int insertAllColumn(ReAward award);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReAward award);

    int updateAllColumnByKey(ReAward award);

    ReAward getByKey(@Param(value = "id") Long id);

    List<ReAward> listAllByCriteria(@Param(value = "criteria") ReAwardQueryCriteria criteria);

    List<ReAward> listAllByCriteriaPage(@Param(value = "page") Page<ReAward> page, @Param(value = "criteria") ReAwardQueryCriteria criteria);

    int updateEnableFlag(ReAward award);
}
