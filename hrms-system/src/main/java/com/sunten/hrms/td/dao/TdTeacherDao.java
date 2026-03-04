package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdTeacher;
import com.sunten.hrms.td.dto.TdTeacherQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.td.vo.TeacherVo;
import com.sunten.hrms.td.dto.TeachingReportQueryCriteria;
import com.sunten.hrms.td.vo.TeachingReportVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训讲师列表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-06-16
 */
@Mapper
@Repository
public interface TdTeacherDao extends BaseMapper<TdTeacher> {

    int insertAllColumn(TdTeacher teacher);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdTeacher teacher);

    int updateAllColumnByKey(TdTeacher teacher);

    TdTeacher getByKey(@Param(value = "id") Long id);

    List<TdTeacher> listAllByCriteria(@Param(value = "criteria") TdTeacherQueryCriteria criteria);

    List<TdTeacher> listAllByCriteriaPage(@Param(value = "page") Page<TdTeacher> page, @Param(value = "criteria") TdTeacherQueryCriteria criteria);

    TdTeacher getByEmployeeId(@Param(value = "employeeId") Long employeeId);

    List<TeachingReportVo> listTeachingByCriteria(@Param(value = "criteria") TeachingReportQueryCriteria criteria);

    List<TeacherVo> listTeacherNoAuth(@Param(value = "criteria") TdTeacherQueryCriteria criteria);
}
