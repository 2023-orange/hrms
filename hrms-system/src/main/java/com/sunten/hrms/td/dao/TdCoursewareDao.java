package com.sunten.hrms.td.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.td.domain.TdCourseware;
import com.sunten.hrms.td.domain.TdCoursewareEmployee;
import com.sunten.hrms.td.dto.TdCoursewareQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课件资料表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-06-18
 */
@Mapper
@Repository
public interface TdCoursewareDao extends BaseMapper<TdCourseware> {

    int insertAllColumn(TdCourseware courseware);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdCourseware courseware);

    int updateAllColumnByKey(TdCourseware courseware);

    TdCourseware getByKey(@Param(value = "id") Long id);

    List<TdCourseware> listAllByCriteria(@Param(value = "criteria") TdCoursewareQueryCriteria criteria);

    List<TdCourseware> listAllByCriteriaPage(@Param(value = "page") Page<TdCourseware> page, @Param(value = "criteria") TdCoursewareQueryCriteria criteria);

    int updateByApproval(TdCourseware courseware); // 审批结果回填

    TdCourseware getByOaOrder(@Param(value = "oaOrder") String oaOrder); // 根据OA审批单号获取


    /* 课程内人员相关接口*/
    int insertCoursewareEmployeeList(List<TdCoursewareEmployee> employees); // 批量插入

    List<TdCoursewareEmployee> getEmployeeByCoursewareId(@Param(value = "coursewareId") Long coursewareId); // 查询

    int deleteEmployeeNotInList(@Param(value = "ids")List<Long> ids,@Param(value = "coursewareId") Long coursewareId); // 删除不在ID集合内的数据
}
