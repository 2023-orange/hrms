package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcBeLateDate;
import com.sunten.hrms.ac.dto.AcBeLateDateQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 厂车迟到时间记录表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-07-08
 */
@Mapper
@Repository
public interface AcBeLateDateDao extends BaseMapper<AcBeLateDate> {

    int insertAllColumn(AcBeLateDate beLateDate);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcBeLateDate beLateDate);

    int updateAllColumnByKey(AcBeLateDate beLateDate);

    AcBeLateDate getByKey(@Param(value = "id") Long id);
    // 查询当天日期的迟到时间对象
    AcBeLateDate getByDay(@Param(value = "lateDate") LocalDate lateDate);

    List<AcBeLateDate> listAllByCriteria(@Param(value = "criteria") AcBeLateDateQueryCriteria criteria);

    List<AcBeLateDate> listAllByCriteriaPage(@Param(value = "page") Page<AcBeLateDate> page, @Param(value = "criteria") AcBeLateDateQueryCriteria criteria);

    // 失效该记录
    int updateEnableByKey(AcBeLateDate beLateDate);
}
