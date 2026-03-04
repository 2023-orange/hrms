package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReDemandJobDescribes;
import com.sunten.hrms.re.dto.ReDemandJobDescribesQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 岗位说明书表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-04-23
 */
@Mapper
@Repository
public interface ReDemandJobDescribesDao extends BaseMapper<ReDemandJobDescribes> {

    int insertAllColumn(ReDemandJobDescribes demandJobDescribes);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReDemandJobDescribes demandJobDescribes);

    int updateAllColumnByKey(ReDemandJobDescribes demandJobDescribes);

    ReDemandJobDescribes getByKey(@Param(value = "id") Long id);

    List<ReDemandJobDescribes> listAllByCriteria(@Param(value = "criteria") ReDemandJobDescribesQueryCriteria criteria);

    List<ReDemandJobDescribes> listAllByCriteriaPage(@Param(value = "page") Page<ReDemandJobDescribes> page, @Param(value = "criteria") ReDemandJobDescribesQueryCriteria criteria);

    int updateColumnByValue(ReDemandJobDescribes reDemandJobDescribes);

    Integer checkCheckFlagAfterUpdate(@Param(value = "jobIds")List<Long> jobIds);
}
