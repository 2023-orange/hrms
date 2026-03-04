package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeAward;
import com.sunten.hrms.pm.dto.PmEmployeeAwardQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 奖罚情况表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeAwardDao extends BaseMapper<PmEmployeeAward> {

    int insertAllColumn(PmEmployeeAward employeeAward);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeAward employeeAward);

    int updateAllColumnByKey(PmEmployeeAward employeeAward);

    PmEmployeeAward getByKey(@Param(value = "id") Long id);

    List<PmEmployeeAward> listAllByCriteria(@Param(value = "criteria") PmEmployeeAwardQueryCriteria criteria);

    List<PmEmployeeAward> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeAward> page, @Param(value = "criteria") PmEmployeeAwardQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeAward employeeAward);

    List<PmEmployeeAward> superQuery(@Param(value = "queryValue")String queryValue);

    int interfaceToMain(@Param(value = "userId")Long userId, @Param(value = "groupId")Long groupId);
}
