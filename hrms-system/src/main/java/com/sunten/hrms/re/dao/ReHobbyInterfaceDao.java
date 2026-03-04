package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReHobbyInterface;
import com.sunten.hrms.re.dto.ReHobbyInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 技术爱好临时表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReHobbyInterfaceDao extends BaseMapper<ReHobbyInterface> {

    int insertAllColumn(ReHobbyInterface hobbyInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReHobbyInterface hobbyInterface);

    int updateAllColumnByKey(ReHobbyInterface hobbyInterface);

    ReHobbyInterface getByKey(@Param(value = "id") Long id);

    List<ReHobbyInterface> listAllByCriteria(@Param(value = "criteria") ReHobbyInterfaceQueryCriteria criteria);

    List<ReHobbyInterface> listAllByCriteriaPage(@Param(value = "page") Page<ReHobbyInterface> page, @Param(value = "criteria") ReHobbyInterfaceQueryCriteria criteria);
}
