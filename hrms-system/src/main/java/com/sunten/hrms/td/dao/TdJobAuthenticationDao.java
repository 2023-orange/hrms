package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdJobAuthentication;
import com.sunten.hrms.td.domain.TdJobGrading;
import com.sunten.hrms.td.dto.TdJobAuthenticationQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.td.dto.TdJobGradingQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 上岗认证表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-06-22
 */
@Mapper
@Repository
public interface TdJobAuthenticationDao extends BaseMapper<TdJobAuthentication> {

    int insertAllColumn(TdJobAuthentication jobAuthentication);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdJobAuthentication jobAuthentication);

    int updateAllColumnByKey(TdJobAuthentication jobAuthentication);

    TdJobAuthentication getByKey(@Param(value = "id") Long id);

    List<TdJobAuthentication> listAllByCriteria(@Param(value = "criteria") TdJobAuthenticationQueryCriteria criteria);

    List<TdJobAuthentication> listAllByCriteriaPage(@Param(value = "page") Page<TdJobAuthentication> page, @Param(value = "criteria") TdJobAuthenticationQueryCriteria criteria);
    // 获取三个月内到期的上岗认证集合
    List<TdJobAuthentication> getDueToRemindAuthentication();

    int interfaceToMain(@Param(value = "userId")Long userId, @Param(value = "groupId")Long groupId);

    List<TdJobAuthentication> getAuthentication(@Param(value = "criteria") TdJobGradingQueryCriteria criteria);

    int updateEnableFlag(TdJobAuthentication jobAuthentication);
}
