package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmGdgz;
import com.sunten.hrms.swm.dto.SwmGdgzQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 旧的固定工资表 Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-07
 */
@Mapper
@Repository
public interface SwmGdgzDao extends BaseMapper<SwmGdgz> {

    int insertAllColumn(SwmGdgz gdgz);

    int deleteByKey();

    int deleteByEntityKey(SwmGdgz gdgz);

    int updateAllColumnByKey(SwmGdgz gdgz);

    SwmGdgz getByKey();

    List<SwmGdgz> listAllByCriteria(@Param(value = "criteria") SwmGdgzQueryCriteria criteria);

    List<SwmGdgz> listAllByCriteriaPage(@Param(value = "page") Page<SwmGdgz> page, @Param(value = "criteria") SwmGdgzQueryCriteria criteria);

    // 查看旧的固定工资
    List<SwmGdgz> getOldFixedSalary(@Param(value = "workCard") String workCard, @Param(value="year") String year);


}
