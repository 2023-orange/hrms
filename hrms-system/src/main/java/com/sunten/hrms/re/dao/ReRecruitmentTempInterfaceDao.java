package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReRecruitmentTempInterface;
import com.sunten.hrms.re.dto.ReRecruitmentTempInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 应聘全部数据导入的临时表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-09-08
 */
@Mapper
@Repository
public interface ReRecruitmentTempInterfaceDao extends BaseMapper<ReRecruitmentTempInterface> {

    int insertAllColumn(ReRecruitmentTempInterface recruitmentTempInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReRecruitmentTempInterface recruitmentTempInterface);

    int updateAllColumnByKey(ReRecruitmentTempInterface recruitmentTempInterface);

    ReRecruitmentTempInterface getByKey(@Param(value = "id") Long id);

    List<ReRecruitmentTempInterface> listAllByCriteria(@Param(value = "criteria") ReRecruitmentTempInterfaceQueryCriteria criteria);

    List<ReRecruitmentTempInterface> listAllByCriteriaPage(@Param(value = "page") Page<ReRecruitmentTempInterface> page, @Param(value = "criteria") ReRecruitmentTempInterfaceQueryCriteria criteria);
}
