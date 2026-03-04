package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeePhoto;
import com.sunten.hrms.pm.dto.PmEmployeePhotoQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 人员图像表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2020-09-09
 */
@Mapper
@Repository
public interface PmEmployeePhotoDao extends BaseMapper<PmEmployeePhoto> {

    int insertAllColumn(PmEmployeePhoto employeePhoto);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeePhoto employeePhoto);

    int updateAllColumnByKey(PmEmployeePhoto employeePhoto);

    PmEmployeePhoto getByKey(@Param(value = "id") Long id);

    List<PmEmployeePhoto> listAllByCriteria(@Param(value = "criteria") PmEmployeePhotoQueryCriteria criteria);

    List<PmEmployeePhoto> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeePhoto> page, @Param(value = "criteria") PmEmployeePhotoQueryCriteria criteria);
}
