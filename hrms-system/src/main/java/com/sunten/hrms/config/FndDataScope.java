package com.sunten.hrms.config;

import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.dto.FndDeptDTO;
import com.sunten.hrms.fnd.dto.FndRoleSmallDTO;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndRoleService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.PmEmployeeJobDao;
import com.sunten.hrms.pm.domain.PmEmployeeJob;
import com.sunten.hrms.pm.dto.PmEmployeeJobDTO;
import com.sunten.hrms.pm.service.PmEmployeeJobService;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.pm.vo.PmLeaderVo;
import com.sunten.hrms.pm.vo.PmMsgVo;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据权限配置
 *
 * @author batan
 * @since 2019-12-10
 */
@Component
public class FndDataScope {

    @Value("${role.authReCharge}")
    private String authReCharge;
    @Value("${role.authPmCharge}")
    private String authPmCharge;
    @Value("${role.authTrainCharge}")
    private String authTrainCharge;
    @Value("${role.admin}")
    private String admin;
    @Value("${role.suntenManagerPermission}")
    private String suntenManagerPermission;

    private final String[] scopeType = {"全部", "本级", "本级及以下", "本人", "自定义"};

    private final FndUserService fndUserService;

    private final FndRoleService fndRoleService;

    private final FndDeptService fndDeptService;

    private final PmEmployeeJobService pmEmployeeJobService;

    private final JwtPermissionService jwtPermissionService;

    private final PmEmployeeService pmEmployeeService;

    private final PmEmployeeJobDao pmEmployeeJobDao;

    public FndDataScope(FndUserService fndUserService, FndRoleService fndRoleService,
                        FndDeptService fndDeptService, PmEmployeeJobService pmEmployeeJobService,
                        JwtPermissionService jwtPermissionService, PmEmployeeService pmEmployeeService, PmEmployeeJobDao pmEmployeeJobDao) {
        this.fndUserService = fndUserService;
        this.fndRoleService = fndRoleService;
        this.fndDeptService = fndDeptService;
        this.pmEmployeeJobService = pmEmployeeJobService;
        this.jwtPermissionService = jwtPermissionService;
        this.pmEmployeeService = pmEmployeeService;
        this.pmEmployeeJobDao = pmEmployeeJobDao;
    }

    /**
     * @return 数据范围deptId集合
     * <p>
     * 当有全部数据权限，则返回空集合
     * 当有部门权限，则返回有权限的deptId集合
     * 当仅有本人数据权限，无关联员工则返回集合为0L，有关联员工，则返回集合[0L, -employeeId]（注意employeeId取负）
     */
    public Set<Long> getDeptIds() {
        return getDeptIds(false, true);
    }


    public Set<Long> getDeptIds(Boolean isMySelfAsMyDept, Boolean checkRole) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        // 用于存储部门id
        Set<Long> deptIds = new HashSet<>();
        Long employeeId = null;
        if (checkRole) {
            // 查询用户角色
            List<FndRoleSmallDTO> roleSet = fndRoleService.listByUserId(user.getId());
            for (FndRoleSmallDTO role : roleSet) {
                // 全部的数据权限
                if (scopeType[0].equals(role.getDataScope())) {
                    return new HashSet<>();
                }
                // 存储本人数据权限
                if (scopeType[3].equals(role.getDataScope())) {
                    deptIds.add(0L);
                }
                // 存储自定义的数据权限
                if (scopeType[4].equals(role.getDataScope())) {
                    Set<FndDept> depts = fndDeptService.listByRoleId(role.getId());
                    for (FndDept dept : depts) {
                        deptIds.addAll(fndDeptService.listAllEnableChildrenIdByPid(dept.getId()));
                    }
                }
            }
        }
        if (user.getEmployee() != null) {
            employeeId = user.getEmployee().getId();
            // 查询用户岗位
            List<PmEmployeeJobDTO> jobs = pmEmployeeJobService.listByEmpIdAndEnabledFlagWithExtend(employeeId, true);
            for (PmEmployeeJobDTO job : jobs) {
                // 全部的数据权限
                if (scopeType[0].equals(job.getJob().getDataScope())) {
                    return new HashSet<>();
                }
                // 存储本级的数据权限
                if (scopeType[1].equals(job.getJob().getDataScope())) {
                    deptIds.add(job.getDept().getId());
                }
                // 存储本级及以下的数据权限
                if (scopeType[2].equals(job.getJob().getDataScope())) {
                    deptIds.addAll(fndDeptService.listAllEnableChildrenIdByPid(job.getDept().getId()));
                }
                // 存储本人数据权限
                if (scopeType[3].equals(job.getJob().getDataScope())) {
                    if (isMySelfAsMyDept) {
                        deptIds.add(job.getDept().getId());
                    } else {
                        deptIds.add(0L);
                    }
                }
                // 存储本人数据权限
                if (scopeType[4].equals(job.getJob().getDataScope())) {
                    Set<FndDept> depts = fndDeptService.listByJobId(job.getJob().getId());
                    for (FndDept dept : depts) {
                        deptIds.addAll(fndDeptService.listAllEnableChildrenIdByPid(dept.getId()));
                    }
                }
            }
        }
        if (deptIds != null && deptIds.contains(0L) && deptIds.size() == 1) {
            if (employeeId != null) {
                deptIds.add(-employeeId);
            }
        } else {
            deptIds.remove(0L);
        }
        return deptIds;
    }


//    public List<Long> getDeptChildren(List<FndDept> deptList) {
//        List<Long> list = new ArrayList<>();
//        deptList.forEach(dept -> {
//                    if (dept != null && dept.getEnabledFlag()) {
//                        List<FndDept> depts = fndDeptService.listByPid(dept.getId());
//                        if (deptList.size() != 0) {
//                            list.addAll(getDeptChildren(depts));
//                        }
//                        list.add(dept.getId());
//                    }
//                }
//        );
//        return list;
//    }


    public boolean isNoDataPermission(FndDataScopeVo dataScopeVo) {
        // 查询条件限制
        Set<Long> deptSetToQuery = new HashSet<>();
        if (!ObjectUtils.isEmpty(dataScopeVo.getDeptAllId())) {
            deptSetToQuery.addAll(fndDeptService.listAllEnableChildrenIdByPid(dataScopeVo.getDeptAllId()));
        }
        if (!ObjectUtils.isEmpty(dataScopeVo.getDeptId())) {
            deptSetToQuery.add(dataScopeVo.getDeptId());
        }
        // 数据权限
        Set<Long> deptIdsInDataScope = getDeptIds(dataScopeVo.getIsMySelfAsMyDept(), dataScopeVo.getIsCheckRole());
        // 数据权限只有本人
        if (deptIdsInDataScope != null && deptIdsInDataScope.contains(0L)) {
            if (deptIdsInDataScope.size() == 1) {
                return true;
            } else {
                deptIdsInDataScope.forEach(id -> {
                    if (!id.equals(0L)) {
                        if (dataScopeVo.getEmployeeId() == null) {
                            dataScopeVo.setEmployeeId(-id);
                        } else {
                            if (!dataScopeVo.getEmployeeId().equals(-id)) {
                                dataScopeVo.setEmployeeId(-1L);
                            }
                        }
                    }
                });
                deptIdsInDataScope = new HashSet<>();
            }
        }
        // 数据权限
//        else if (deptIdsInDataScope != null && deptIdsInDataScope.size() == 0) {
//            deptIdsInDataScope = new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(1L));
//        }
        Set<Long> result = new HashSet<>();
        // 查询条件不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(deptIdsInDataScope) && !CollectionUtils.isEmpty(deptSetToQuery)) {
            // 取交集
            result.addAll(deptSetToQuery);
            result.retainAll(deptIdsInDataScope);
            // 若无交集，则代表无数据权限
            if (result.size() == 0) {
                return true;
            }
        } else {
            // 否则取并集
            result.addAll(deptSetToQuery);
            if (null != deptIdsInDataScope) {
                result.addAll(deptIdsInDataScope);
            }
        }
        dataScopeVo.setDeptIds(result);
        return false;
    }

    public boolean checkPermissions(String... permissions) {
        // 获取当前用户的所有权限
        List<String> elPermissions = SecurityUtils.getUserDetails().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return Arrays.stream(permissions).anyMatch(elPermissions::contains);
    }

    // 给首页出工作台的本级及以下使用
    public Boolean checkLevelAndBelow(FndUserDTO fndUser) {
        if (fndUser.getEmployee() != null) {
            // 查询用户岗位
            List<PmEmployeeJobDTO> jobs = pmEmployeeJobService.listByEmpIdAndEnabledFlagWithExtend(fndUser.getEmployee().getId(), true);
            for (PmEmployeeJobDTO job : jobs) {
                if (!scopeType[3].equals(job.getJob().getDataScope())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean checkIsLeader(FndUserDTO user) {
        try {
            List<FndDeptDTO> fndDepCharge = fndDeptService.getDeptByEmpAndAdminJob(user.getEmployee().getId());
            return fndDepCharge.size() > 0 || fndUserService.checkUserHaveRole(suntenManagerPermission, user.getEmployee().getId());
        } catch(NullPointerException e) {
            return false;
        }

    }

    public Map<String,Object> getUserMesForSendRequest(String workCard, Boolean checkRoleFlag, Boolean pmChargeFlag, Boolean tdChargeFlag,
                                                       Boolean reChargeFlag, Boolean selfFlag, Boolean hrSpecialist,Boolean recruitmentSpecialist) {
        Map<String, Object> map = new HashMap<>();
        FndUserDTO user;
        if (null != workCard) {
            user = fndUserService.getByName(workCard);
        } else {
            user = fndUserService.getByName(SecurityUtils.getUsername());
        }
        if (null == user.getEmployee()) { // 空直接返回
            return map;
        }
        map.put("userId", user.getId());
        map.put("userName", user.getEmployee().getWorkCard());
        if (checkRoleFlag) {
            Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
            for (GrantedAuthority auth : grantedAuthorityCollection
            ) {
                // 角色判定
                if (auth.getAuthority().equals(authReCharge)) {
                    map.put("recruitmentRoleFlag", true);
                }
                if (auth.getAuthority().equals(authPmCharge)) {
                    map.put("pmChargeFlag", true);
                }
                // .....
            }
        }
        if (pmChargeFlag) {
            // 获取人事专员的wokrCard集合
            Set<String> pmChargeWorkCardSet = pmEmployeeService.getWorkListByRolePermission(authPmCharge);
            map.put("pmChargeList", pmChargeWorkCardSet);
        }
        if (tdChargeFlag) {
            // 获取培训专员的workCard集合(2023.10.26改为岗位集合)
            Set<String> tdChargeWorkCardSet = pmEmployeeJobDao.listJobEmployeeByName("教学计划实施专员");
            map.put("tdChargeList", tdChargeWorkCardSet);
        }
        if (reChargeFlag) {
            // 获取招聘专员的workCard集合
            Set<String> reChargeWorkCardSet = pmEmployeeService.getWorkListByRolePermission(authReCharge);
            map.put("reChargeList", reChargeWorkCardSet);
        }
        if (hrSpecialist) {
            // 获取人事专员岗位的wokrCard集合
            Set<String> hrSpecialistWorkCardSet = pmEmployeeJobDao.listJobEmployeeByName("人事专员");
            map.put("hrSpecialist", hrSpecialistWorkCardSet);
        }
        if (recruitmentSpecialist) {
            // 获取招聘专员岗位的wokrCard集合
            Set<String> recruitmentSpecialistWorkCardSet = pmEmployeeJobDao.listJobEmployeeByName("招聘专员");
            map.put("recruitmentSpecialist", recruitmentSpecialistWorkCardSet);
        }
        // 获取薪酬专员的workCard集合(2023.10.26改为岗位集合)
        Set<String> swmChargeWorkCardSet = pmEmployeeJobDao.listJobEmployeeByName("薪酬专员");
        map.put("swmChargeList", swmChargeWorkCardSet);

        // 获取厂医的workCard集合
//        Set<String> doctorWorkCardSet = pmEmployeeService.getWorkListByRolePermission("doctor");
//        map.put("doctorList", doctorWorkCardSet);


        if (selfFlag) {
            // 主管经理判定, 根据userId获取
            PmEmployeeJob pmEmployeeJob = pmEmployeeJobService.getManagerOrSupervisor(user.getId());
            if (pmEmployeeJob.getManagerFlag()) {
                map.put("manager", user.getEmployee().getWorkCard());
            }
            if (pmEmployeeJob.getSupervisorFlag()) {
                map.put("supervisor", user.getEmployee().getWorkCard());
            }
            map.put("salesFlag", pmEmployeeJob.getSalesFlag());
            // 根据userId获取员工信息
            PmMsgVo pmMsgVo = pmEmployeeService.getPmMsgByUserId(user.getId());
            // 根据userId获取员工的直属主管及经理
            PmLeaderVo pmLeaderVo = pmEmployeeService.getCurrentManagerAndSuperior(user.getId());
            if (null != pmLeaderVo && null != pmLeaderVo.getCurrentManager()) {
                map.put("myManagerWorkCard", pmLeaderVo.getCurrentManager());
            }
            if (null != pmLeaderVo && null != pmLeaderVo.getCurrentManager()) {
                map.put("mySupervisorWorkCard", pmLeaderVo.getCurrentSupervisor());
            }
            map.put("leaderFlag", null != pmLeaderVo && (pmLeaderVo.getLeaderFlag()));
            map.put("pmMsg", pmMsgVo);
        }
        return map;
    }

    public Map<String, Object> checkRolePermission(String rolePermission) {
        Map<String, Object> map = new HashMap<>();
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        map.put("bool", false);
        for (GrantedAuthority auth: grantedAuthorityCollection) {
            if (auth.getAuthority().equals(rolePermission) || auth.getAuthority().equals(admin)) {
                map.put("bool", true);
            }
        }
        map.put("employeeId", null != user.getEmployee() ? user.getEmployee().getId() : -1);
        map.put("userId", user.getId());
        map.put("deptId", user.getDept().getId());
        return map;
    }

    /**
     *  @author liangjw
     *  @since 2021/12/1 15:39
     *  全局使用的设置高级查询、普通查询及人事管辖范围的查询
     * @param queryCriteria
     */
//    public void setQueryCriteria(DxnjQueryCriteria queryCriteria) {
//        FndDataScopeVo dataScopeVo = new FndDataScopeVo(queryCriteria.getDeptAllId(), queryCriteria.getDeptId(), false, true, queryCriteria.getDeptIds(), queryCriteria.getEmployeeId());
//        if (this.isNoDataPermission(dataScopeVo)) {
//            queryCriteria.setNullFlag(true);
//        } else {
//            queryCriteria.setNullFlag(false);
//        }
//        queryCriteria.setDeptIds(dataScopeVo.getDeptIds());
//        queryCriteria.setEmployeeId(dataScopeVo.getEmployeeId());
//        if (null != queryCriteria.getAdvancedQuerysStr() && !"".equals(queryCriteria.getAdvancedQuerysStr())) {
//            JSONArray jsonArray = new JSONArray(queryCriteria.getAdvancedQuerysStr());
//            queryCriteria.setAdvancedQuerys(jsonArray.toList(AdvancedQuery.class));
//        }
//    }
}
