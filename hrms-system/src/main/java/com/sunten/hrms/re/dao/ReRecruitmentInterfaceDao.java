package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReRecruitmentInterface;
import com.sunten.hrms.re.dto.ReRecruitmentInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 招骋数据临时表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReRecruitmentInterfaceDao extends BaseMapper<ReRecruitmentInterface> {

    int insertAllColumn(ReRecruitmentInterface recruitmentInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReRecruitmentInterface recruitmentInterface);

    int updateAllColumnByKey(ReRecruitmentInterface recruitmentInterface);

    ReRecruitmentInterface getByKey(@Param(value = "id") Long id);

    List<ReRecruitmentInterface> listAllByCriteria(@Param(value = "criteria") ReRecruitmentInterfaceQueryCriteria criteria);

    List<ReRecruitmentInterface> listAllByCriteriaPage(@Param(value = "page") Page<ReRecruitmentInterface> page, @Param(value = "criteria") ReRecruitmentInterfaceQueryCriteria criteria);
}
