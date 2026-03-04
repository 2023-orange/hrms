package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReEducation;
import com.sunten.hrms.re.dto.ReEducationQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 受教育经历表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReEducationDao extends BaseMapper<ReEducation> {

    int insertAllColumn(ReEducation education);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReEducation education);

    int updateAllColumnByKey(ReEducation education);

    ReEducation getByKey(@Param(value = "id") Long id);

    List<ReEducation> listAllByCriteria(@Param(value = "criteria") ReEducationQueryCriteria criteria);

    List<ReEducation> listAllByCriteriaPage(@Param(value = "page") Page<ReEducation> page, @Param(value = "criteria") ReEducationQueryCriteria criteria);

    int updateEnableFlag(ReEducation education);
}
