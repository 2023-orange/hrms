package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReVocational;
import com.sunten.hrms.re.dto.ReVocationalQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 招聘职业资格表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2020-08-28
 */
@Mapper
@Repository
public interface ReVocationalDao extends BaseMapper<ReVocational> {

    int insertAllColumn(ReVocational vocational);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReVocational vocational);

    int updateAllColumnByKey(ReVocational vocational);

    ReVocational getByKey(@Param(value = "id") Long id);

    List<ReVocational> listAllByCriteria(@Param(value = "criteria") ReVocationalQueryCriteria criteria);

    List<ReVocational> listAllByCriteriaPage(@Param(value = "page") Page<ReVocational> page, @Param(value = "criteria") ReVocationalQueryCriteria criteria);

    int updateEnableFalg(ReVocational vocational);
}
