package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.dto.PmEmployeeLikeQueryCriteria;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.pm.vo.CheckPmEmployeeVo;
import com.sunten.hrms.pm.vo.PmLeaderVo;
import com.sunten.hrms.pm.vo.PmManagerVo;
import com.sunten.hrms.pm.vo.PmMsgVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 人事档案表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeDao extends BaseMapper<PmEmployee> {

    int insertAllColumn(PmEmployee employee);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployee employee);

    int updateAllColumnByKey(PmEmployee employee);

    int updateEnableFlag(PmEmployee employee);

    PmEmployee getByKey(@Param(value = "id") Long id, @Param(value = "deptIdsInDataScope")Set<Long> deptIdsInDataScope);

    List<PmEmployee> listAllByCriteria(@Param(value = "criteria") PmEmployeeQueryCriteria criteria);

    List<PmEmployee> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployee> page, @Param(value = "criteria") PmEmployeeQueryCriteria criteria);

    List<PmEmployee> listDownloadByCriteriaPage(@Param(value = "page") Page<PmEmployee> page, @Param(value = "criteria") PmEmployeeQueryCriteria criteria);

    int updateLeaveFlag(PmEmployee employee);

    int updateColumnByCheck(PmEmployee employee);

    List<PmEmployee> listCheckByPage(@Param(value = "page") Page<PmEmployee> page,@Param(value = "criteria") PmEmployeeQueryCriteria criteria);

    Long queryEmployeeIsChang(@Param(value = "employeeId")Long employeeId);

    int updatePhotoPath(PmEmployee employee);

    List<PmEmployee> listForIdNumberOrWorkCardExist(@Param(value = "criteria") PmEmployeeQueryCriteria criteria);

    List<PmEmployee> otherNoLeaveofficeEmpInfo(@Param(value = "idNumber")String idNumber,@Param(value = "employeeId")Long employeeId);

    List<PmEmployee> selectByEmpIdList(@Param(value = "idList") Set<Long> idList);

    int batchUpdateCompanyAge();

    List<PmEmployee> listForAttendanceSet(@Param(value = "page") Page<PmEmployee> page, @Param(value = "criteria") PmEmployeeQueryCriteria criteria);

    int updateEmployeeForAttendanceSet(PmEmployee employee);

    List<PmEmployee> listForSwmEmpUse(@Param(value = "nameOrWorkCard") String  nameOrWorkCard);

    // 根据姓名、工牌号查询员工信息
    List<PmEmployee> listEmployeeByWorkCardOrName(@Param(value = "criteria") PmEmployeeLikeQueryCriteria criteria);
    // 根据身份证号获取该员工是否还存在其他离职信息
    List<PmEmployee> otherLeaveEmpInfoByIdNumber(@Param(value = "idNumber")String idNumber,@Param(value = "employeeId")Long employeeId);
    // 根据身份证号获取最新一条离职信息
    PmEmployee newLeaveofficeEmployeeInfo(@Param(value = "idNumber")String idNumber);

    void executeCopyEmployeeChildrenProcedure(@Param(value = "oldEmployeeId")Long oldEmployeeId, @Param(value = "nowEmployeeId")Long nowEmployeeId, @Param(value = "userId")Long userId);

    void executeDisabledEmployeeChildrenProcedure(@Param(value = "employeeId")Long employeeId, @Param(value = "userId")Long userId);

    // 获取管理岗人员信息
    List<PmEmployee> listLeaderEmployee();

    // 根据工牌或姓名获取人员信息
    List<PmEmployee> listEmployeesBaseInfoList(@Param(value = "criteria") PmEmployeeQueryCriteria criteria);

    // 根据userId获取员工信息集合
    PmMsgVo getPmMsgByUserId(@Param(value = "userId")Long userId);

    // 根据userId获取员工的领导工号
    PmLeaderVo getCurrentManagerAndSuperior(@Param(value = "userId")Long userId);

    // 根据deptId获取主管领导
    PmLeaderVo getManagerAndSuperiorByDeptId(@Param(value = "deptId")Long deptId);

    // 根据role的permission获取当前拥有该role的人员的workcard集合
    Set<String> getWorkListByRolePermission(@Param(value = "permission") String permission);

    // 根据role获取当前人员的email
    Set<String> getEmailListByRolePermission(@Param(value = "permisiion") String permission);

    // 根据deptId获取该部门负责人的email
    String getEmailByDeptId(@Param(value = "deptId")Long deptId);

    // 根据deptId获取该部门的上级部门负责人的email
    String getEmailByDeptParentId(@Param(value = "deptId")Long deptId);

    //根据部门科室班组获取当前的班长信息
    List<PmManagerVo> getTeamByDeptNameAndDepartmentNameAndTeamName(@Param(value="map")Map<String, Object> map);
//    @Param(value="deptName") String deptName, @Param(value="departmentName") String departmentName,
//    @Param(value="teamName") String teamName, @Param(value="workCard") String workCard

    // 根据部门科室获取当前的主管信息
    List<PmManagerVo> getSuperiorByDeptNameAndDepartmentName(@Param(value="map")Map<String, Object> map);


    // 更具部门获取当前的经理信息
    List<PmManagerVo> getManagerByDeptName(@Param(value="map")Map<String, Object> map);

    // 根据部门获取领导
    List<PmManagerVo> getLeader();

    PmEmployee getLeaderByDeptId(@Param(value="deptId") Long deptId);

    List<PmEmployee> superQuery(@Param(value = "queryValue") String queryValue);

    // 更新在职人员的带薪年假
    void updatePaidAnnualLeaveEveryDay(@Param(value = "workCard") String workCard, @Param(value = "dayNum") Integer dayNum);

    // 更新将要离职的在职人员的带薪年假
    void updatePaidAnnualLeaveForWillLeaveEveryDay(@Param(value = "workCard") String workCard, @Param(value = "dayNum") Integer dayNum);

    CheckPmEmployeeVo checkPmEmployeeBeforeUpdate(@Param(value = "workCard") String workCard, @Param(value = "idNumber") String idNumber,
                                                  @Param(value = "id") Long id);
    // 更新满十年社保时间
    void updateTenYearDate(PmEmployee pe);
    // 置空满十年社保
    void updateTenYearDateSetNull(PmEmployee pe);

    List<PmEmployee> getPmEmailList(@Param(value = "page") Page<PmEmployee> page, @Param(value = "criteria") PmEmployeeQueryCriteria criteria);

    void savePmEmail(PmEmployee pmEmployee);

    List<PmEmployee> getPmTelList(@Param(value = "page") Page<PmEmployee> page, @Param(value = "criteria") PmEmployeeQueryCriteria criteria);

    PmEmployee getPmEmployeeByWorkCard(String workCard);
}
