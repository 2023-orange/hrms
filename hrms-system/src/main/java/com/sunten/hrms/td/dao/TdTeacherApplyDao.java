package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdTeacherApply;
import com.sunten.hrms.td.dto.TdTeacherApplyQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 讲师身份（申请）表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-06-15
 */
@Mapper
@Repository
public interface TdTeacherApplyDao extends BaseMapper<TdTeacherApply> {

    int insertAllColumn(TdTeacherApply teacherApply);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdTeacherApply teacherApply);

    int updateAllColumnByKey(TdTeacherApply teacherApply);

    TdTeacherApply getByKey(@Param(value = "id") Long id);

    List<TdTeacherApply> listAllByCriteria(@Param(value = "criteria") TdTeacherApplyQueryCriteria criteria);

    List<TdTeacherApply> listAllByCriteriaPage(@Param(value = "page") Page<TdTeacherApply> page, @Param(value = "criteria") TdTeacherApplyQueryCriteria criteria);
    // 讲师认证接口
    int updateAttestation(TdTeacherApply teacherApply);
    // 根据申请单获取
    TdTeacherApply getByReqCode(@Param(value = "reqCode") String reqCode);
    // 审批结束时
    int updateByApprovalEnd(TdTeacherApply teacherApply);
    // 中间节点审批时
    int updateByApprovalUnderwar(TdTeacherApply teacherApply);

    List<TdTeacherApply> listByEnable(@Param(value = "employeeId") Long employeeId);
}
