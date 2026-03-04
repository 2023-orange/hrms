package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndJob;
import com.sunten.hrms.fnd.domain.FndJobAndDept;
import com.sunten.hrms.fnd.dto.FndJobQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Mapper
@Repository
public interface FndJobDao extends BaseMapper<FndJob> {

    int insertAllColumn(FndJob job);

    int deleteByKey(@Param(value = "id") Long id);

    int updateAllColumnByKey(FndJob job);

    FndJob getByKey(@Param(value = "id") Long id);

    List<FndJob> listAllByCriteria(@Param(value = "criteria") FndJobQueryCriteria criteria);

    List<FndJob> listAllByCriteriaPage(@Param(value = "page") Page<FndJob> page, @Param(value = "criteria") FndJobQueryCriteria criteria);

    int updateDeletedFlag(FndJob job);

    void updateSortJob(FndJob job);

    Long getMaxJobSequence();
    // 大于等于所传数据的自动+1
    void thanAutoIncrement(@Param(value = "sequence")Long sequence);

    // 在俩数值范围内的自动+1,适用于往前插入时
    void inAutoIncrement(@Param(value = "minSequence")Long minSequence, @Param(value = "maxSequence")Long maxSequence);

    // 在俩数值范围内的自动-1，适用往后插入时
    void inLessen(@Param(value = "minSequence")Long minSequence, @Param(value = "maxSequence")Long maxSequence);

    // 删除岗位时自减1
    void lessenJobSequence(@Param(value = "sequence")Long sequence);

    List<FndJob> listByAdminJob(@Param(value = "criteria") FndJobQueryCriteria criteria);

    List<FndJobAndDept> listFndJobAndDept();

    /**
     * 获取认证岗位List
     * @return
     */
    List<HashMap<String, Object>> loadAllCertificationJobList();
}
