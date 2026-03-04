package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReEducationInterface;
import com.sunten.hrms.re.dto.ReEducationInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 受教育经历临时表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReEducationInterfaceDao extends BaseMapper<ReEducationInterface> {

    int insertAllColumn(ReEducationInterface educationInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReEducationInterface educationInterface);

    int updateAllColumnByKey(ReEducationInterface educationInterface);

    ReEducationInterface getByKey(@Param(value = "id") Long id);

    List<ReEducationInterface> listAllByCriteria(@Param(value = "criteria") ReEducationInterfaceQueryCriteria criteria);

    List<ReEducationInterface> listAllByCriteriaPage(@Param(value = "page") Page<ReEducationInterface> page, @Param(value = "criteria") ReEducationInterfaceQueryCriteria criteria);
}
