package com.sunten.hrms.fnd.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunten.hrms.fnd.domain.FndVisit;
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
 * @since 2019-12-20
 */
@Mapper
@Repository
public interface FndVisitDao extends BaseMapper<FndVisit> {

    int insertAllColumn(FndVisit visit);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndVisit visit);

    int updateAllColumnByKey(FndVisit visit);

    FndVisit getByKey(@Param(value = "id") Long id);

    FndVisit getByDate(String date);

    List<FndVisit> listVisitBetween(String date1, String date2);
}
