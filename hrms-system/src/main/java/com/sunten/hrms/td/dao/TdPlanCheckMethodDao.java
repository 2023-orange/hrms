package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdPlanCheckMethod;
import com.sunten.hrms.td.dto.TdPlanCheckMethodQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训效果评价方式表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2022-03-08
 */
@Mapper
@Repository
public interface TdPlanCheckMethodDao extends BaseMapper<TdPlanCheckMethod> {

    int insertAllColumn(TdPlanCheckMethod planCheckMethod);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdPlanCheckMethod planCheckMethod);

    int updateAllColumnByKey(TdPlanCheckMethod planCheckMethod);

    TdPlanCheckMethod getByKey(@Param(value = "id") Long id);

    List<TdPlanCheckMethod> listAllByCriteria(@Param(value = "criteria") TdPlanCheckMethodQueryCriteria criteria);

    List<TdPlanCheckMethod> listAllByCriteriaPage(@Param(value = "page") Page<TdPlanCheckMethod> page, @Param(value = "criteria") TdPlanCheckMethodQueryCriteria criteria);

    int deleteByMethodAndEnabledFlag(@Param(value = "checkMethod")String checkMethod, @Param(value = "planImplementId")Long planImplementId,
                                     @Param(value = "updateBy")Long updateBy);

    int updateEvaluationResultsByMethodAndPlanImplementId(TdPlanCheckMethod tdPlanCheckMethod);
}
