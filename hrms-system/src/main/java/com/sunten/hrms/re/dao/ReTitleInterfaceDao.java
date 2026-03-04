package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReTitleInterface;
import com.sunten.hrms.re.dto.ReTitleInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 职称情况临时表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReTitleInterfaceDao extends BaseMapper<ReTitleInterface> {

    int insertAllColumn(ReTitleInterface titleInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReTitleInterface titleInterface);

    int updateAllColumnByKey(ReTitleInterface titleInterface);

    ReTitleInterface getByKey(@Param(value = "id") Long id);

    List<ReTitleInterface> listAllByCriteria(@Param(value = "criteria") ReTitleInterfaceQueryCriteria criteria);

    List<ReTitleInterface> listAllByCriteriaPage(@Param(value = "page") Page<ReTitleInterface> page, @Param(value = "criteria") ReTitleInterfaceQueryCriteria criteria);
}
