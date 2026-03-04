package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdJobGrading;
import com.sunten.hrms.td.dto.TdJobGradingQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 岗位分级表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2022-03-22
 */
@Mapper
@Repository
public interface TdJobGradingDao extends BaseMapper<TdJobGrading> {

    int insertAllColumn(TdJobGrading jobGrading);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdJobGrading jobGrading);

    int updateAllColumnByKey(TdJobGrading jobGrading);

    TdJobGrading getByKey(@Param(value = "id") Long id);

    List<TdJobGrading> listAllByCriteria(@Param(value = "criteria") TdJobGradingQueryCriteria criteria);

    List<TdJobGrading> listAllByCriteriaPage(@Param(value = "page") Page<TdJobGrading> page, @Param(value = "criteria") TdJobGradingQueryCriteria criteria);

    List<String> listForProcess(@Param(value = "criteria") TdJobGradingQueryCriteria criteria); // 获取工序集合

    List<TdJobGrading> listForJob(@Param(value = "criteria") TdJobGradingQueryCriteria criteria); // 获取岗位集合

    List<TdJobGrading> getCertificationJobByProcess(@Param(value = "criteria") TdJobGradingQueryCriteria criteria); // 获取认证岗位集合

    Integer checkInfo(@Param("jobName") String jobName, @Param("certificationJob") String certificationJob,@Param("deptId") Long deptId);

}
