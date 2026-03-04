package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcSetUp;
import com.sunten.hrms.ac.dto.AcSetUpQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 考勤异常允许设置表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-10-23
 */
@Mapper
@Repository
public interface AcSetUpDao extends BaseMapper<AcSetUp> {

    int insertAllColumn(AcSetUp setUp);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcSetUp setUp);

    int updateAllColumnByKey(AcSetUp setUp);

    AcSetUp getByKey(@Param(value = "id") Long id);

    List<AcSetUp> listAllByCriteria(@Param(value = "criteria") AcSetUpQueryCriteria criteria);

    List<AcSetUp> listAllByCriteriaPage(@Param(value = "page") Page<AcSetUp> page, @Param(value = "criteria") AcSetUpQueryCriteria criteria);

    /**
     *  @author：liangjw
     *  @Date: 2020/10/23 10:40
     *  @Description: 取最新一条
     */
    AcSetUp getByNew();
}
