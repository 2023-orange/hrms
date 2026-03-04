package com.sunten.hrms.ac.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunten.config.DataSourceKeyEnum;
import com.sunten.config.annotation.DataSource;
import com.sunten.hrms.ac.domain.AcOvertimeReview;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AcOvertimeReviewDao extends BaseMapper<AcOvertimeReview> {
    @DataSource(DataSourceKeyEnum.OA)
    List<AcOvertimeReview> getAcOvertimeReviewList();
}
