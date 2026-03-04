package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndServerInformation;
import com.sunten.hrms.fnd.dto.FndServerInformationQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 服务器信息表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2024-06-06
 */
@Mapper
@Repository
public interface FndServerInformationDao extends BaseMapper<FndServerInformation> {

    int insertAllColumn(FndServerInformation serverInformation);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndServerInformation serverInformation);

    int updateAllColumnByKey(FndServerInformation serverInformation);

    FndServerInformation getByKey(@Param(value = "id") Long id);

    List<FndServerInformation> listAllByCriteria(@Param(value = "criteria") FndServerInformationQueryCriteria criteria);

    List<FndServerInformation> listAllByCriteriaPage(@Param(value = "page") Page<FndServerInformation> page, @Param(value = "criteria") FndServerInformationQueryCriteria criteria);
}
