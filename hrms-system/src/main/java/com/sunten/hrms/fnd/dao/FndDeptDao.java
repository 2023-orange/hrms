package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.dto.FndDeptQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.fnd.vo.DeptEmp;
import com.sunten.hrms.fnd.vo.PieVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Mapper
@Repository
public interface FndDeptDao extends BaseMapper<FndDept> {

    int insertAllColumn(FndDept dept);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndDept dept);

    int updateAllColumnByKey(FndDept dept);

    FndDept getByKey(@Param(value = "id") Long id);

    List<FndDept> listAllByCriteria(@Param(value = "criteria") FndDeptQueryCriteria criteria);

    List<FndDept> listAllByCriteriaPage(@Param(value = "page") Page<FndDept> page, @Param(value = "criteria") FndDeptQueryCriteria criteria);

    String getNameById(Long id);

    List<FndDept> listByPid(Long id);

    List<FndDept> listAllChildrenByPid(Long id);

    List<FndDept> listByRoleId(Long id);

    int updateDeletedFlag(FndDept dept);

    void updateSort(FndDept dept);

    List<PieVo> getPieEducation(@Param(value = "deptIds") List<Long> deptIds);

    List<Double> getPieAge(@Param(value = "deptIds") List<Long> deptIds);

    List<Double> getPieWorkAge(@Param(value = "deptIds") List<Long> deptIds);

    List<PieVo> getPieOccupationCategory(@Param(value = "deptIds") List<Long> deptIds);

    List<PieVo> getPieVocationalLevel(@Param(value = "deptIds") List<Long> deptIds);

    List<PieVo> getPieOccupationType(@Param(value = "deptIds") List<Long> deptIds);

    int updateDeptExtend(Long id);

    List<FndDept> listByJobId(Long id);
    // 获取同层次部门最大序号
    Long getMaxDeptSequence(Long pId);

    // 同层次部门大于等于的序号自动+1
    void thanAutoIncrement(Long sequence,Long pId);

    // 同层次部门在俩数值范围内的序号自动+1,适用于往前插入时
    void inAutoIncrement(Long minSequence, Long maxSequence,Long pId);

    // 同层次部门在俩数值范围内的序号自动-1，适用往后插入时
    void inLessen(Long minSequence, Long maxSequence, Long pId);

    // 删除部门时同层部门的序号自减1
    void lessenDeptSequence(Long sequence,Long pId);
    // 获取公司领导的部门ID
    FndDept getDeptByLeader();

    // 获取当前员工的管理岗挂哪个部门
    List<FndDept> getDeptByEmpAndAdminJob(Long employeeId);

    DeptEmp getPmByAdminJobId(@Param(value = "deptId")Long deptId);

    List<FndDept> getDeptByCriteria(@Param(value = "criteria") FndDeptQueryCriteria criteria);
    // 获取当前生效的部门及其管理岗是否有人
    List<FndDept> getDeptByAdminBy();
    // 根据人员id获取其所在部门及管理人员信息
    DeptEmp getDeptAndAdminByEmployeeId(@Param(value = "employeeId")Long employeeId);

    // 根据人员id获取其所在部门
    DeptEmp getDeptByEmployeeId(@Param(value = "employeeId")Long employeeId);

    // 获取生效的部门及其管理人员信息
    List<DeptEmp> getDeptAndAdminEmployee();

    // 根据id获取部门及对应管理人员信息
    DeptEmp getDeptAndEmployeeById(@Param(value = "deptId")Long deptId);

    List<FndDept> getDeptListByIds(@Param(value = "ids") List<Long> ids);

    // 创建临时快照
    void createTempSnap(@Param(value = "id")Long id, @Param(value = "timestamp")String timestamp);

    // 删除临时快照
    void deleteTempSnap(@Param(value = "timestamp")String timestamp);

    // 根据临时快照生成“机构调整”岗位调动记录
    int insertOrgAdjJobTransfer(@Param(value = "timestamp")String timestamp,
                                @Param(value = "updateBy")Long updateBy,
                                @Param(value = "updateTime")LocalDateTime updateTime,
                                @Param(value = "remarks")String remarks);

}
