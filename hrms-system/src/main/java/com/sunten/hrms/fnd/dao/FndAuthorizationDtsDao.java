package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.fnd.domain.FndAuthorizationDts;
import com.sunten.hrms.fnd.dto.FndAuthorizationDtsQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-02-02
 */
@Mapper
@Repository
public interface FndAuthorizationDtsDao extends BaseMapper<FndAuthorizationDts> {

    int insertAllColumn(FndAuthorizationDts authorizationDts);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndAuthorizationDts authorizationDts);

    int updateAllColumnByKey(FndAuthorizationDts authorizationDts);

    FndAuthorizationDts getByKey(@Param(value = "id") Long id);

    List<FndAuthorizationDts> listAllByCriteria(@Param(value = "criteria") FndAuthorizationDtsQueryCriteria criteria);

    List<FndAuthorizationDts> listAllByCriteriaPage(@Param(value = "page") Page<FndAuthorizationDts> page, @Param(value = "criteria") FndAuthorizationDtsQueryCriteria criteria);

    // 根据员工ID查询被授权的部门id集合
    List<Long> getAuthorizationDeptsByToEmployee(@Param(value = "toId") Long toId);

    int batchInsertAllColumn(@Param(value = "authorizationDts") List<FndAuthorizationDts> authorizationDts);

    int batchUpdateEnalbleFlag(@Param(value = "authorizationDts")FndAuthorizationDts authorizationDts);
}
