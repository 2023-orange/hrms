package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcFakeRecordSetting;
import com.sunten.hrms.ac.dto.AcFakeRecordSettingQueryCriteria;
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
 * @since 2021-12-22
 */
@Mapper
@Repository
public interface AcFakeRecordSettingDao extends BaseMapper<AcFakeRecordSetting> {

    int insertAllColumn(AcFakeRecordSetting fakeRecordSetting);

    int deleteByKey(@Param(value = "username") String username);

    int deleteByEntityKey(AcFakeRecordSetting fakeRecordSetting);

    int updateAllColumnByKey(AcFakeRecordSetting fakeRecordSetting);

    int updateEnabled(AcFakeRecordSetting fakeRecordSetting);

    AcFakeRecordSetting getByKey(@Param(value = "username") String username);

    List<AcFakeRecordSetting> listAllByCriteria(@Param(value = "criteria") AcFakeRecordSettingQueryCriteria criteria);

    List<AcFakeRecordSetting> listAllByCriteriaPage(@Param(value = "page") Page<AcFakeRecordSetting> page, @Param(value = "criteria") AcFakeRecordSettingQueryCriteria criteria);
}
