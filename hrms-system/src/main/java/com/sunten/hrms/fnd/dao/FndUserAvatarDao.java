package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndUserAvatar;
import com.sunten.hrms.fnd.dto.FndUserAvatarQueryCriteria;
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
public interface FndUserAvatarDao extends BaseMapper<FndUserAvatar> {

    int insertAllColumn(FndUserAvatar userAvatar);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndUserAvatar userAvatar);

    int updateAllColumnByKey(FndUserAvatar userAvatar);

    FndUserAvatar getByKey(@Param(value = "id") Long id);

    List<FndUserAvatar> listAllByCriteria(@Param(value = "criteria") FndUserAvatarQueryCriteria criteria);

    List<FndUserAvatar> listAllByCriteriaPage(@Param(value = "page") Page<FndUserAvatar> page, @Param(value = "criteria") FndUserAvatarQueryCriteria criteria);
}
