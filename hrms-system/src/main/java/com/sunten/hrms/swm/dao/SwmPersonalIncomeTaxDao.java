package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmPersonalIncomeTax;
import com.sunten.hrms.swm.domain.SwmPersonalIncomeTaxInterface;
import com.sunten.hrms.swm.dto.SwmPersonalIncomeTaxQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 个人所得税表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmPersonalIncomeTaxDao extends BaseMapper<SwmPersonalIncomeTax> {

    int insertAllColumn(SwmPersonalIncomeTax personalIncomeTax);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmPersonalIncomeTax personalIncomeTax);

    int updateAllColumnByKey(SwmPersonalIncomeTax personalIncomeTax);

    SwmPersonalIncomeTax getByKey(@Param(value = "id") Long id);

    List<SwmPersonalIncomeTax> listAllByCriteria(@Param(value = "criteria") SwmPersonalIncomeTaxQueryCriteria criteria);

    List<SwmPersonalIncomeTax> listAllByCriteriaPage(@Param(value = "page") Page<SwmPersonalIncomeTax> page, @Param(value = "criteria") SwmPersonalIncomeTaxQueryCriteria criteria);

    int interfaceToMain(@Param(value = "groupId") Long groupId);

    int interfaceToMainWithNotAmount(@Param(value = "groupId") Long groupId);

    int deleteByIncomePeriod(@Param(value = "incomePeriod") String incomePeriod, @Param(value = "amountFlag")Boolean amountFlag);

    List<SwmPersonalIncomeTax> getTaxListByUserId(@Param(value = "userId")Long userId);

    // 用于出右上方period
    String selectTop2Period();
}
