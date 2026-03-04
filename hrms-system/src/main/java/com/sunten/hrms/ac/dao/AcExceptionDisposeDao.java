package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcExceptionDispose;
import com.sunten.hrms.ac.dto.AcExceptionDisposeQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 考勤异常及处理表 Mapper 接口
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Mapper
@Repository
public interface AcExceptionDisposeDao extends BaseMapper<AcExceptionDispose> {

    int insertAllColumn(AcExceptionDispose exceptionDispose);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcExceptionDispose exceptionDispose);

    int updateAllColumnByKey(AcExceptionDispose exceptionDispose);

    int updateSomeColumnByKey(AcExceptionDispose exceptionDispose);

    AcExceptionDispose getByKey(@Param(value = "id") Long id);

    List<AcExceptionDispose> listAllByCriteria(@Param(value = "criteria") AcExceptionDisposeQueryCriteria criteria);

    List<AcExceptionDispose> listAllByCriteriaPage(@Param(value = "page") Page<AcExceptionDispose> page, @Param(value = "criteria") AcExceptionDisposeQueryCriteria criteria);
}
