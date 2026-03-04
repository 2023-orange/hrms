package com.sunten.hrms.re.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.re.domain.ReDemandAgreeNum;
import com.sunten.hrms.re.dto.ReDemandAgreeNumQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2022-11-22
 */
@Mapper
@Repository
public interface ReDemandAgreeNumDao extends BaseMapper<ReDemandAgreeNum> {

    int insertAllColumn(ReDemandAgreeNum reDemandAgreeNum);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReDemandAgreeNum reDemandAgreeNum);

    ReDemandAgreeNum getByKey(@Param(value = "id") Long id);

    List<ReDemandAgreeNum> listAllByCriteria(@Param(value = "criteria") ReDemandAgreeNumQueryCriteria criteria);

    List<ReDemandAgreeNum> listAllByCriteriaPage(@Param(value = "page") Page<ReDemandAgreeNum> page, @Param(value = "criteria") ReDemandAgreeNumQueryCriteria criteria);
}
