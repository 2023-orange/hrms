package com.sunten.hrms.pm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunten.hrms.pm.domain.PmPhoto;
import com.sunten.hrms.pm.dto.PmPhotoQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PmPhotoDao extends BaseMapper<PmPhoto> {
    List<PmPhoto> listAllByCriteria(@Param(value = "criteria")PmPhotoQueryCriteria pmPhotoQueryCriteria);
}
