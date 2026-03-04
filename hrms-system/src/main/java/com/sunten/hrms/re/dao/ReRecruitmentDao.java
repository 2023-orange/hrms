package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReRecruitment;
import com.sunten.hrms.re.dto.ReRecruitmentQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 招骋数据表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReRecruitmentDao extends BaseMapper<ReRecruitment> {

    int insertAllColumn(ReRecruitment recruitment);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReRecruitment recruitment);

    int updateAllColumnByKey(ReRecruitment recruitment);

    ReRecruitment getByKey(@Param(value = "id") Long id);

    List<ReRecruitment> listAllByCriteria(@Param(value = "criteria") ReRecruitmentQueryCriteria criteria);

    List<ReRecruitment> listAllByCriteriaPage(@Param(value = "page") Page<ReRecruitment> page, @Param(value = "criteria") ReRecruitmentQueryCriteria criteria);

    int updateEnableFlag(ReRecruitment recruitment);

    void insertByTempInterface(@Param(value = "groupId")Long groupId);
}
