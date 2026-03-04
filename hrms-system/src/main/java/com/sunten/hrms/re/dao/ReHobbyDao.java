package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReHobby;
import com.sunten.hrms.re.dto.ReHobbyQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 技术爱好表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReHobbyDao extends BaseMapper<ReHobby> {

    int insertAllColumn(ReHobby hobby);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReHobby hobby);

    int updateAllColumnByKey(ReHobby hobby);

    ReHobby getByKey(@Param(value = "id") Long id);

    List<ReHobby> listAllByCriteria(@Param(value = "criteria") ReHobbyQueryCriteria criteria);

    List<ReHobby> listAllByCriteriaPage(@Param(value = "page") Page<ReHobby> page, @Param(value = "criteria") ReHobbyQueryCriteria criteria);

    int updateEnableFlag(ReHobby hobby);
}
