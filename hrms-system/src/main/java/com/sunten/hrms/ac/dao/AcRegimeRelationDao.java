package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcRegimeRelation;
import com.sunten.hrms.ac.dto.AcRegimeRelationQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 考勤制度排班时间关系表 Mapper 接口
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Mapper
@Repository
public interface AcRegimeRelationDao extends BaseMapper<AcRegimeRelation> {

    int insertAllColumn(AcRegimeRelation regimeRelation);
    /**
     *  @author：liangjw
     *  @Date: 2020/9/22 11:53
     *  @Description: 批量插入
     *  @params:
     */
    int insertCollection(List<AcRegimeRelation> acRegimeRelations);

    /**
     *  @author：liangjw
     *  @Date: 2020/9/22 11:53
     *  @Description: 批量删除
     *  @params:
     */
    int deleteCollection(List<AcRegimeRelation> acRegimeRelations);

    /**
     *  @author：liangjw
     *  @Date: 2020/9/22 11:54
     *  @Description: 单个删除
     *  @params:
     */
    int deleteByEnabled(AcRegimeRelation acRegimeRelation);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcRegimeRelation regimeRelation);

    int updateAllColumnByKey(AcRegimeRelation regimeRelation);

    int deleteByRegimeIdAndRegimeTimeId(AcRegimeRelation regimeRelation);

    AcRegimeRelation getByKey(@Param(value = "id") Long id);

    List<AcRegimeRelation> listAllByCriteria(@Param(value = "criteria") AcRegimeRelationQueryCriteria criteria);

    List<AcRegimeRelation> listAllByCriteriaPage(@Param(value = "page") Page<AcRegimeRelation> page, @Param(value = "criteria") AcRegimeRelationQueryCriteria criteria);
}
