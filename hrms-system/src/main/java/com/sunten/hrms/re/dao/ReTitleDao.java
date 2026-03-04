package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReTitle;
import com.sunten.hrms.re.dto.ReTitleQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 职称情况表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReTitleDao extends BaseMapper<ReTitle> {

    int insertAllColumn(ReTitle title);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReTitle title);

    int updateAllColumnByKey(ReTitle title);

    ReTitle getByKey(@Param(value = "id") Long id);

    List<ReTitle> listAllByCriteria(@Param(value = "criteria") ReTitleQueryCriteria criteria);

    List<ReTitle> listAllByCriteriaPage(@Param(value = "page") Page<ReTitle> page, @Param(value = "criteria") ReTitleQueryCriteria criteria);

    int updateEnableFlag(ReTitle title);

    void insertByTempInterface(@Param(value = "groupId")Long groupId);
}
