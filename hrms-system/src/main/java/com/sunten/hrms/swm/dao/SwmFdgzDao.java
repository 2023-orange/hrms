package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmFdgz;
import com.sunten.hrms.swm.dto.SwmFdgzQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 旧的浮动工资表 Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-10
 */
@Mapper
@Repository
public interface SwmFdgzDao extends BaseMapper<SwmFdgz> {

    int insertAllColumn(SwmFdgz fdgz);

    int deleteByKey();

    int deleteByEntityKey(SwmFdgz fdgz);

    int updateAllColumnByKey(SwmFdgz fdgz);

    SwmFdgz getByKey();

    List<SwmFdgz> listAllByCriteria(@Param(value = "criteria") SwmFdgzQueryCriteria criteria);

    List<SwmFdgz> listAllByCriteriaPage(@Param(value = "page") Page<SwmFdgz> page, @Param(value = "criteria") SwmFdgzQueryCriteria criteria);

    // 查看旧的浮动工资
    List<SwmFdgz> getOldFloatSalary(@Param(value = "workCard") String workCard, @Param(value="year") String year);
}
