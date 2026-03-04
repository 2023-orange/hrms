package com.sunten.hrms.fnd.service.impl;

import com.sunten.hrms.ac.dao.AcAttendanceRecordHistoryDao;
import com.sunten.hrms.ac.dao.AcEmpDeptsDao;
import com.sunten.hrms.ac.dao.AcEmployeeAttendanceDao;
import com.sunten.hrms.ac.domain.AcAttendanceRecordHistory;
import com.sunten.hrms.ac.domain.AcEmpDepts;
import com.sunten.hrms.ac.dto.AcAttendanceRecordHistoryQueryCriteria;
import com.sunten.hrms.ac.dto.AcEmpDeptsQueryCriteria;
import com.sunten.hrms.ac.rest.AcAttendanceRecordHistoryController;
import com.sunten.hrms.ac.vo.NoAttendanceDetailVo;
import com.sunten.hrms.ac.vo.NoAttendanceEmialHeader;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndAuthorizationDtsDao;
import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.dao.FndDictDetailDao;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.dto.FndDeptQueryCriteria;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndAgencyService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.fnd.vo.FndAgencyVo;
import com.sunten.hrms.pm.dao.*;
import com.sunten.hrms.pm.domain.*;
import com.sunten.hrms.pm.dto.*;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author：liangjw
 * @Date: 2020/11/3 13:35
 * @Description: 生成代办列表
 */
@Service
@CacheConfig(cacheNames = "agency")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndAgencyServiceImpl implements FndAgencyService {
    // 描述定义
    // 个人代办
    private static final String PERSON_DES = "个人代办或通知";
    // 管理人员代办
    private static final String CHARGE_DES = "工作代办";
    // 考勤异常原因填写
    private static final String AC_HISTORY_DES_WRITE = "考勤异常原因填写";
    // 考勤异常审批
    private static final String AC_HISTORY_DES_APPRIVAL = "考勤异常审批";
    //未提交流程审批的离职申请
    private static final String PM_LEAVEAPPROVAL = "未提交流程审批的离职申请";
    //体检申请未提交评审的数据
    private static final String PM_MEDICALAPPROVAL = "未提交流程审批的体检申请";

    // 组件名称
    // 考勤异常填写
    private static final String AC_HISTORY_WRITE_PATH = "DisposeList";
    // 考勤异常审批
    private static final String AC_HISTORY_APPROVAL_PATH = "AppraisalList";
    // 人员合同
    private static final String PM_CONTRATC_PATH = "LaborContract";
    // 人员调动
    private static final String PM_TRANSFER_PATH = "JobTransfer";
    // 人员入职
    private static final String PM_ENTRY_PATH = "PmEntry";
    // 员工排班
    private static final String AC_EMPATTENDANCE_PATH = "EmployeeAttendance";
    //离职信息
    private static final String PM_LEAVE_PATH = "LeaveApprovalList";
    //体检信息
    private static final String PM_MEDICAL_PATH = "MedicalRequest";

    // 权限码定义
    // 人资考勤管理员
    @Value("${role.authRegime}")
    private String AUTH_AC_MAIN;
    // 人资人事管理员
    @Value("${role.authPmCharge}")
    private String AUTH_PM;
    // 资料员
    @Value("${role.authDocumenter}")
    private String AUTH_DOC;
    // 班长
    @Value("${role.authTeam}")
    private String AUTH_TEAM;


    private static final Integer COUNT = 10; // 数据限制量
    private Integer acHisWritePersonCount = 0; //  个人异常填写数量
    private Integer entryCount = 0; // 试用期数量
    private Integer contractCount = 0; // 合同数量
    private Integer transferCount = 0; // 调动数量
    private Integer leaveApprovalCount = 0; //离职申请数量
    private Integer medicalApprovalCount = 0;//体检申请数量

    private final AcAttendanceRecordHistoryDao acAttendanceRecordHistoryDao;
    private final FndUserService fndUserService;
    private final JwtPermissionService jwtPermissionService;
    private final FndDataScope fndDataScope;
    private final FndDictDetailDao fndDictDetailDao;
    private final PmEmployeeContractDao pmEmployeeContractDao;
    private final PmEmployeeEntryDao pmEmployeeEntryDao;
    private final PmEmployeeJobTransferDao pmEmployeeJobTransferDao;
    private final AcEmpDeptsDao acEmpDeptsDao;
    private final AcEmployeeAttendanceDao acEmployeeAttendanceDao;
    private final FndDeptDao fndDeptDao;
    private final FndAuthorizationDtsDao fndAuthorizationDtsDao;
    private final PmLeaveApprovalDao pmLeaveApprovalDao;
    private final PmMedicalDao pmMedicalDao;

    public FndAgencyServiceImpl(AcAttendanceRecordHistoryDao acAttendanceRecordHistoryDao,
                                FndUserService fndUserService, JwtPermissionService jwtPermissionService,
                                FndDataScope fndDataScope, FndDictDetailDao fndDictDetailDao,
                                PmEmployeeContractDao pmEmployeeContractDao,
                                PmEmployeeEntryDao pmEmployeeEntryDao,
                                PmEmployeeJobTransferDao pmEmployeeJobTransferDao,
                                AcEmpDeptsDao acEmpDeptsDao,
                                AcEmployeeAttendanceDao acEmployeeAttendanceDao,
                                FndDeptDao fndDeptDao,
                                FndAuthorizationDtsDao fndAuthorizationDtsDao,
                                PmLeaveApprovalDao pmLeaveApprovalDao,
                                PmMedicalDao pmMedicalDao) {
        this.acAttendanceRecordHistoryDao = acAttendanceRecordHistoryDao;
        this.fndUserService = fndUserService;
        this.jwtPermissionService = jwtPermissionService;
        this.fndDataScope = fndDataScope;
        this.fndDictDetailDao = fndDictDetailDao;
        this.pmEmployeeContractDao = pmEmployeeContractDao;
        this.pmEmployeeEntryDao = pmEmployeeEntryDao;
        this.pmEmployeeJobTransferDao = pmEmployeeJobTransferDao;
        this.acEmpDeptsDao = acEmpDeptsDao;
        this.acEmployeeAttendanceDao = acEmployeeAttendanceDao;
        this.fndDeptDao = fndDeptDao;
        this.fndAuthorizationDtsDao = fndAuthorizationDtsDao;
        this.pmLeaveApprovalDao = pmLeaveApprovalDao;
        this.pmMedicalDao = pmMedicalDao;
    }

    @Override
    public Map<String, Object> buildTree(List<FndAgencyVo> menuVos) {
        List<FndAgencyVo> menuTrees = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        Set<Long> ids = new HashSet<>();
        for (FndAgencyVo agencyVo : menuVos) {
            if (agencyVo.getParentId() == 0L) {
                menuTrees.add(agencyVo);
            }
            for (FndAgencyVo it : menuVos) {
                if (it.getParentId().equals(agencyVo.getId())) {
                    if (agencyVo.getChildren() == null) {
                        agencyVo.setChildren(new ArrayList<>());
                    }
                    agencyVo.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        menuTrees = menuTrees.size() == 0 ? menuVos.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList()) : menuTrees;
        map.put("content", menuTrees);
        map.put("totalElements", menuVos.size());
        return map;
    }

    @Override
    public List<FndAgencyVo> getList() {
        // 考勤管理员
        boolean acMainFlag = false;
        // 人事管理员
        boolean pmFlag = false;
        // 班组长
        boolean teamFlag = false;
        // 资料员
        boolean docFlag = false;
        // 领导标记
        boolean chargeFlag = false;
        // 授权标记
        boolean authFlag = false;
        // 厂医标记
        boolean doctorFlag =false;
        // 日期转换
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 自增id
        Long count = 3L;
        // 存储所有menu的集合
        List<FndAgencyVo> targetMenu = new ArrayList<>();
        // 根据用户获取人员信息
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        // 获取权限集合
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        // 组建个人list
        // 获取个人异常
        if (null == user.getDept()) {
            throw new InfoCheckWarningMessException("该用户没有绑定员工，无法查看待办列表，请联系管理员。");
        }
        // 检验是否班组长
        AcEmpDeptsQueryCriteria acEmpDeptsQueryCriteria = new AcEmpDeptsQueryCriteria();
        acEmpDeptsQueryCriteria.setDataType(AUTH_TEAM);
        acEmpDeptsQueryCriteria.setEnabledFlag(true);
        acEmpDeptsQueryCriteria.setEmployeeId(user.getEmployee().getId());
        Set<Long> teamDeptIds = acEmpDeptsDao.listAllByCriteria(acEmpDeptsQueryCriteria).stream().map(AcEmpDepts::getDeptId).collect(Collectors.toSet());
        if (teamDeptIds.size() > 0) {
            teamFlag = true;
        }
        // 检验是否资料员
        acEmpDeptsQueryCriteria.setDataType(AUTH_DOC);
        Set<Long> docDeptIds = acEmpDeptsDao.listAllByCriteria(acEmpDeptsQueryCriteria).stream().map(AcEmpDepts::getDeptId).collect(Collectors.toSet());
        if (docDeptIds.size() > 0) {
            docFlag = true;
        }
        // 获取管理岗, 可以用于判定是否为管理人员
        List<FndDept> fndDepCharge = fndDeptDao.getDeptByEmpAndAdminJob(user.getEmployee().getId());
        // 获取个人填写异常
        AcAttendanceRecordHistoryQueryCriteria acAttendanceRecordHistoryQueryCriteria = new AcAttendanceRecordHistoryQueryCriteria();
        acAttendanceRecordHistoryQueryCriteria.setPersonEmployeeId(user.getEmployee().getId());
        acAttendanceRecordHistoryQueryCriteria.setPersonUnion(true);
        if (fndDepCharge.size() > 0) { // 管理岗的人要查person及退回
            acAttendanceRecordHistoryQueryCriteria.setChara("charge");
        } else { // 普通员工只查为普通人员
            if (docFlag) {
                acAttendanceRecordHistoryQueryCriteria.setChara("documentor");
            } else {
                acAttendanceRecordHistoryQueryCriteria.setChara("person");
            }
        }
        acHisWritePersonCount = acAttendanceRecordHistoryDao.inUsedCountHistory(acAttendanceRecordHistoryQueryCriteria);
        // 获取合同快到期通知（个人）
        // 拿出合同快到期天数：30
        String contractDay = fndDictDetailDao.selectDetailByNameAndLabel("pm_constract_time", "天数").getValue();
        String PM_CONTRACT_DES = contractDay + "天内结束的合同";
        LocalDate contractEndDay = LocalDate.now().plusDays(Long.parseLong(contractDay));
        List<PmEmployeeContract> pmEmployeeContracts = getPersonPmEmployeeContracts(user, contractEndDay);
        //离职申请
        //定义离职申请未提交状态notCommit
        String approvalStatus = "notCommit";
        String PM_LEAVE_DES = "未提交流程审批的离职申请";
        Boolean submitFlag = false;
        String PM_MEDICAL_DES = "未提交流程审批的体检申请";
        // 获取试用期快到期通知
        // 拿出试用期快到期天数
        String entryDay = fndDictDetailDao.selectDetailByNameAndLabel("pm_entry_time", "pm_entry_time").getValue();
        String PM_ENTRY_DES = "试用期在" + entryDay + "天内结束";
        LocalDate entryEndDay = LocalDate.now().plusDays(Long.parseLong(entryDay));
        List<PmEmployeeEntry> pmEmployeeEntries = getPersonPmEmployeeEntries(user, entryEndDay);
        // 岗位调动
        String PM_TRANSFER_DES = "30天内结束的调动";
        LocalDate pmTransferDay = LocalDate.now().plusDays(30L);
        // 获取个人调动
        List<PmEmployeeJobTransfer> pmEmployeeJobTransfers = getPersonPmEmployeeJobTransfers(user, pmTransferDay);
        // 创建个人层待办
        count = createPerson(fmt, count, targetMenu, user, acAttendanceRecordHistoryQueryCriteria, PM_CONTRACT_DES, pmEmployeeContracts, PM_ENTRY_DES, pmEmployeeEntries, PM_TRANSFER_DES, pmEmployeeJobTransfers);

        // 开始组建工作待办List, 根据权限码、及数据范围获取异常数据
        // 人员排班信息用到的变量
        String PM_ATTENDANCE_DES = "下一天未排班员工数量";
        NoAttendanceEmialHeader noAttendanceEmialHeaderTarget = new NoAttendanceEmialHeader();
        int noAttendaceEmpCount = 0;
        // 快到期合同的变量
        PmEmployeeContractQueryCriteria pmEmployeeContractQueryCriteriaCharge = new PmEmployeeContractQueryCriteria();
        List<PmEmployeeContract> pmEmployeeContractListCharge = new ArrayList<>();
        // 试用期快到期的变量
        PmEmployeeEntryQueryCriteria entryQueryCriteria = new PmEmployeeEntryQueryCriteria();
        List<PmEmployeeEntry> employeeEntryList = new ArrayList<>();
        // 调动快到期的变量
        PmEmployeeJobTransferQueryCriteria pejQueryCriteria = new PmEmployeeJobTransferQueryCriteria();
        List<PmEmployeeJobTransfer> employeeJobTransfersCharge = new ArrayList<>();
        //未提交流程审批的离职申请变量
        PmLeaveApprovalQueryCriteria pmLeaveApprovalQueryCriteriaCharge = new PmLeaveApprovalQueryCriteria();
        List<PmLeaveApproval> leaveApprovalsCharge = new ArrayList<>();
        //未提交流程审批的体检申请变量
        PmMedicalQueryCriteria pmMedicalApprovalQueryCriteriaCharge = new PmMedicalQueryCriteria();
        int medicalApprovalsCharge = 0;
        // 角色判定
        for (GrantedAuthority grantedAuthority : grantedAuthorityCollection
        ) {
            if (grantedAuthority.getAuthority().equals(AUTH_AC_MAIN)) {
                acMainFlag = true;
            }
            if (grantedAuthority.getAuthority().equals(AUTH_PM)) {
                pmFlag = true;
            }
            if (grantedAuthority.getAuthority().equals("medicalNoData:list")){
                doctorFlag = true;
            }
        }
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, false, null,null);

        // 根据标记与角色数据范围查询相关数据
        // 为人事负责人
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            pmEmployeeContractListCharge = null;
            employeeEntryList = null;
            employeeJobTransfersCharge = null;
            leaveApprovalsCharge = null;
        } else {
            // 如果不为人事负责人，则按照岗位权限范围获取
            pmEmployeeContractListCharge = getPmEmployeeContracts(contractEndDay, pmEmployeeContractQueryCriteriaCharge, pmEmployeeContractListCharge, dataScopeVo, pmFlag);
            employeeEntryList = getPmEmployeeEntries(entryEndDay, entryQueryCriteria, employeeEntryList, dataScopeVo, pmFlag);
            employeeJobTransfersCharge = getPmEmployeeJobTransfers(pmTransferDay, pejQueryCriteria, employeeJobTransfersCharge, dataScopeVo, pmFlag);
            leaveApprovalsCharge = getPmLeaveApprovals(approvalStatus, pmLeaveApprovalQueryCriteriaCharge, leaveApprovalsCharge, dataScopeVo, pmFlag);
            medicalApprovalsCharge = getPmMedicalApprovals(submitFlag, pmMedicalApprovalQueryCriteriaCharge, medicalApprovalsCharge, dataScopeVo, pmFlag,doctorFlag);
        }

        // 获取有员工没排班的收件人email
        List<NoAttendanceEmialHeader> noAttendanceEmialHeaderList = acEmployeeAttendanceDao.getNoAttendanceEmailHeader(user.getEmployee().getId());
        if (noAttendanceEmialHeaderList.size() > 0) {
            noAttendanceEmialHeaderTarget = noAttendanceEmialHeaderList.get(0);
        }
        if (null != noAttendanceEmialHeaderTarget && null != noAttendanceEmialHeaderTarget.getDeptId()) {
            // 根据deptId获取没有排班人员
            List<FndDept> noAttendanceDeptList = fndDeptDao.listAllChildrenByPid(noAttendanceEmialHeaderTarget.getDeptId());
            List<NoAttendanceDetailVo> noAttendanceDetailVos = acEmployeeAttendanceDao.getNoAttendanceDetail(noAttendanceDeptList.stream().map(FndDept::getId).collect(Collectors.toList()));
            noAttendaceEmpCount = noAttendanceDetailVos.size();
        }

        // 取出部门管理岗上没有任职人员的部门
        FndDeptQueryCriteria fndDeptQueryCriteria = new FndDeptQueryCriteria();
        fndDeptQueryCriteria.setEnabled(true);
        fndDeptQueryCriteria.setDeleted(false);
        fndDeptQueryCriteria.setNoUser(true);
        List<FndDept> fndDepts = fndDeptDao.listAllByCriteria(fndDeptQueryCriteria);
        // 存储交集
        List<FndDept> targets;
        // 存储差集
        List<Long> reduceTargets = new ArrayList<>();
        // 最终的管理范围
        List<Long> totalDept = new ArrayList<>();
        if (fndDataScope.getDeptIds().size() > 0) {
            // 取出交集，即无管理人员且在自身管辖范围内
            List<Long> list = dataScopeVo.getDeptIds().stream().filter(item -> !item.equals(user.getDept().getId())).collect(Collectors.toList());
            targets = fndDepts.stream().filter(fndDept -> list.contains(fndDept.getId())).collect(Collectors.toList());// 取出差集
            for (Long i : list
            ) {
                if (!targets.stream().map(FndDept::getId).collect(Collectors.toList()).contains(i)) {
                    reduceTargets.add(i);
                }
            }
            // 判断 targets中的parentid 在不在差集内， 在要排除，不在就用
            for (FndDept fd: targets
            ) {
                if (!reduceTargets.contains(fd.getParentId())) {
                    totalDept.add(fd.getId());
                }
            }
        }

        // 考勤异常填写部分
        AcAttendanceRecordHistoryQueryCriteria writeQueryCriteria = new AcAttendanceRecordHistoryQueryCriteria();
        // 是班组长，开启team层
        if (teamFlag) {
            writeQueryCriteria.setTeamUnion(true);
            writeQueryCriteria.setTeamDeptIds(teamDeptIds);
            writeQueryCriteria.setTeamCodeStatus("all");
            writeQueryCriteria.setTeamRemoveFlag(true);
            writeQueryCriteria.setChara("team");
            // 分离个人
            writeQueryCriteria.setTeamRemoveSelfFlag(true);
            writeQueryCriteria.setTeamRemoveSelfId(user.getEmployee().getId());
        }
        // 是资料员，开启doc层
        if (docFlag) {
            writeQueryCriteria.setDocUnion(true);
            writeQueryCriteria.setDocDeptIds(docDeptIds);
            writeQueryCriteria.setDocQueryFlag(true);
            writeQueryCriteria.setDocRemoveFlag(true);
        }
        // 异常填写数量
        Integer acHisCount = acAttendanceRecordHistoryDao.inUsedCountHistory(writeQueryCriteria);
        // 获取授权范围
        List<Long> authDeptIds = fndAuthorizationDtsDao.getAuthorizationDeptsByToEmployee(user.getEmployee().getId());
        // 检验领导或授权
        if (fndDepCharge.size() > 0 || totalDept.size() > 0 ) {
            chargeFlag = true;
        }
        if (authDeptIds.size() > 0) {
            authFlag = true;
        }
        // 考勤异常审批
        AcAttendanceRecordHistoryQueryCriteria apprivalQueryCriteria = new AcAttendanceRecordHistoryQueryCriteria();
        // 有领导审批权限
        if (chargeFlag || authFlag) {
            apprivalQueryCriteria.setTeamUnion(true);
            apprivalQueryCriteria.setTeamCommitFlag(true);
            apprivalQueryCriteria.setTeamCodeStatus("leader");
            apprivalQueryCriteria.setTeamRemoveFlag(true);
            Set<Long> targetsTemp = new HashSet<>(totalDept);
            AcAttendanceRecordHistoryController.setTeamDeptIdsAndSetDeptsIsNull(apprivalQueryCriteria, user, authFlag, dataScopeVo, authDeptIds, targetsTemp);
            apprivalQueryCriteria.setTeamDeptIds(targetsTemp);
            // 再合并管理线的管理人员
            if (chargeFlag) {
                apprivalQueryCriteria.setTeamLowerLevel(true);
                apprivalQueryCriteria.setHigherLevelEmployeeId(user.getEmployee().getId());
            }
            // 再合并授权范围
            if (authDeptIds.size() > 0) {
                apprivalQueryCriteria.setTeamAuthLowerLevel(true);
                apprivalQueryCriteria.setTeamAuthDeptIds(authDeptIds);
            }
        }
        if (acMainFlag) {
            // 有考勤管理员权限
            apprivalQueryCriteria.setDocCommitFlag(true);
            apprivalQueryCriteria.setDocUnion(true);
            apprivalQueryCriteria.setDocCodeStatus("acAdmin");
            apprivalQueryCriteria.setDocRemoveFlag(true);
        }
        // 异常审批数量
        Integer acHisApprivalCount = acAttendanceRecordHistoryDao.inUsedCountHistory(apprivalQueryCriteria);
        // 所有查询完毕
        if (acMainFlag || pmFlag || fndDataScope.checkLevelAndBelow(user)  || null != noAttendanceEmialHeaderTarget || acHisCount > 0 || acHisApprivalCount > 0 ) {
            FndAgencyVo fndAgencyVo = new FndAgencyVo(CHARGE_DES, "", "", null, 2L, 0L, null);
            targetMenu.add(fndAgencyVo);
            if ( contractCount > 0
                    || entryCount > 0 || transferCount > 0 || noAttendaceEmpCount > 0 || acHisCount > 0 || acHisApprivalCount > 0 || leaveApprovalCount > 0 || medicalApprovalCount > 0
            ) {
                //  创建异常填写层
                if (acHisCount > 0) {
                    FndAgencyVo acHistoryVoCharge = new FndAgencyVo(AC_HISTORY_DES_WRITE, AC_HISTORY_WRITE_PATH, null,
                            null, count, 2L, (long) acHisCount);
                    Long acPersonCount = count;
                    targetMenu.add(acHistoryVoCharge);
                    count++;
                    // 创建异常填写父子层
                    if (acHisCount < COUNT) {
                        count = generateAcAbnormal(fmt, count, targetMenu, acAttendanceRecordHistoryDao.inUsedListForBatch(writeQueryCriteria), acPersonCount, "write");
                    }
                }
                // 创建异常审批层
                if (acHisApprivalCount > 0) {
                    FndAgencyVo acHistoryVoCharge = new FndAgencyVo(AC_HISTORY_DES_APPRIVAL, AC_HISTORY_APPROVAL_PATH, null,
                            null, count, 2L, (long) acHisApprivalCount);
                    Long acPersonCount = count;
                    targetMenu.add(acHistoryVoCharge);
                    count++;
                    // 创建异常审批父子层
                    if (acHisApprivalCount < COUNT) {
                        count = generateAcAbnormal(fmt, count, targetMenu, acAttendanceRecordHistoryDao.inUsedListForBatch(apprivalQueryCriteria), acPersonCount, "apprival");
                    }
                }
                // 创建合同层
                if (contractCount > 0) {
                    FndAgencyVo pmContractCharge = new FndAgencyVo(PM_CONTRACT_DES, PM_CONTRATC_PATH, null,
                            null, count, 2L, (long) contractCount);
                    Long pmContractChargeCount = count;
                    targetMenu.add(pmContractCharge);
                    count++;
                    // 创建合同父子层
                    if (null != pmEmployeeContractListCharge) {
                        for (PmEmployeeContract pmContract : pmEmployeeContractListCharge
                        ) {
                            FndAgencyVo pmContractVo = new FndAgencyVo("结束日期:" + pmContract.getEndTime().format(fmt) + ",工号:" + pmContract.getEmployee().getWorkCard()
                                    + ",姓名:" + pmContract.getEmployee().getName()
                                    , PM_CONTRATC_PATH,
                                    "{ \"empId\": " + pmContract.getEmployee().getId() + ",\"contractId\":" + pmContract.getId() + "}",
                                    null, count, pmContractChargeCount, null);
                            targetMenu.add(pmContractVo);
                            count++;
                        }
                    }
                }
                // 创建试用期层
                if (entryCount > 0) {
                    FndAgencyVo pmEntryCharge = new FndAgencyVo(PM_ENTRY_DES, PM_ENTRY_PATH, null, null, count, 2L, (long) entryCount);
                    Long pmEntryChargeCount = count;
                    targetMenu.add(pmEntryCharge);
                    count++;
                    // 创建试用父子层
                    if (null != employeeEntryList) {
                        for (PmEmployeeEntry pmEntry : employeeEntryList
                        ) {
                            FndAgencyVo pmEntryVo = new FndAgencyVo("转正日期:" + pmEntry.getFormalTime().format(fmt) + ",工号:" + pmEntry.getEmployee().getWorkCard()
                                    + ",姓名:" + pmEntry.getEmployee().getName(), PM_ENTRY_PATH, "{ \"empId\": " + pmEntry.getEmployee().getId() + ",\"contractId\":" + pmEntry.getId() + "}"
                                    , null, count, pmEntryChargeCount, null);
                            targetMenu.add(pmEntryVo);
                            count++;
                        }
                    }
                }
                // 创建调岗层
                if (transferCount > 0) {
                    FndAgencyVo pejChargeVo = new FndAgencyVo(PM_TRANSFER_DES, PM_TRANSFER_PATH, null, null, count, 2L, (long) transferCount);
                    Long pejChargeCount = count;
                    targetMenu.add(pejChargeVo);
                    count++;
                    // 创建调动父子层
                    if (null != employeeJobTransfersCharge) {
                        for (PmEmployeeJobTransfer pej : employeeJobTransfersCharge
                        ) {
                            FndAgencyVo pejVo = new FndAgencyVo("调动结束日期:" + pej.getEndTime().format(fmt) + ",工号:" + pej.getEmployee().getWorkCard()
                                    + ",姓名:" + pej.getEmployee().getName(), PM_TRANSFER_PATH,
                                    "{ \"empId\": " + user.getEmployee().getId() + ",\"jobTransferId\":" + pej.getId() + "}", null, count, pejChargeCount, null);
                            targetMenu.add(pejVo);
                            count++;
                        }
                    }
                }
                // 创建员工排班层
                if (noAttendaceEmpCount > 0) {
                    FndAgencyVo empAttendanceVo = new FndAgencyVo(PM_ATTENDANCE_DES, AC_EMPATTENDANCE_PATH, null, null, count, 2L, (long) noAttendaceEmpCount);
                    targetMenu.add(empAttendanceVo);
                }
                // 创建离职层
                if (leaveApprovalCount > 0) {
                    FndAgencyVo pmLeaveCharge = new FndAgencyVo(PM_LEAVE_DES, PM_LEAVE_PATH, null, null, count, 2L, (long) leaveApprovalCount);
                    Long pmLeaveChargeCount = count;
                    targetMenu.add(pmLeaveCharge);
                    count++;
                    // 创建离职父子层
                    if (null != leaveApprovalsCharge) {
                        for (PmLeaveApproval pmLea : leaveApprovalsCharge
                        ) {
                            FndAgencyVo pmContractVo = new FndAgencyVo("最后离职日期："+pmLea.getApproveLeaveDate()+",工号:" + pmLea.getEmployeeId()
                                    + ",姓名:" + pmLea.getName(),PM_LEAVE_PATH,
                                    "{ \"empId\": " + pmLea.getId() + ",\"contractId\":" + pmLea.getId() + "}", null, count, pmLeaveChargeCount, null);
                            targetMenu.add(pmContractVo);
                            count++;
                        }
                    }
                }
                // 创建体检层
                if (medicalApprovalCount > 0) {
                    if(doctorFlag||chargeFlag) {
                        FndAgencyVo pmMedicalCharge = new FndAgencyVo(PM_MEDICAL_DES, PM_MEDICAL_PATH, null, null, count, 2L, (long) medicalApprovalCount);
                        Long pmMedicalChargeCount = count;
                        targetMenu.add(pmMedicalCharge);
                        count++;
                    }
                }
            } else {
                FndAgencyVo normalChargeVo = new FndAgencyVo("暂无工作待办", null, null, null, count, 2L, null);
                targetMenu.add(normalChargeVo);
            }
        }
        return targetMenu;
    }

    private List<PmEmployeeJobTransfer> getPmEmployeeJobTransfers(LocalDate pmTransferDay, PmEmployeeJobTransferQueryCriteria pejQueryCriteria, List<PmEmployeeJobTransfer> employeeJobTransfersCharge, FndDataScopeVo dataScopeVo, Boolean pmFlag) {
        setPejQueryCriteria(pmTransferDay, pejQueryCriteria, dataScopeVo, pmFlag);
        transferCount = pmEmployeeJobTransferDao.countJobTransfer(pejQueryCriteria);
        if (transferCount < COUNT) {
            employeeJobTransfersCharge = pmEmployeeJobTransferDao.listAllByCriteria(pejQueryCriteria);
        }
        return employeeJobTransfersCharge;
    }

    //获取员工离职情况
    private List<PmLeaveApproval> getPmLeaveApprovals(String approvalStatus, PmLeaveApprovalQueryCriteria pmLeaveApprovalQueryCriteriaCharge, List<PmLeaveApproval> leaveApprovalsCharge,FndDataScopeVo dataScopeVo, Boolean pmFlag) {
        setpmLeaveApprovalQueryCriteriaCharge(approvalStatus, pmLeaveApprovalQueryCriteriaCharge, dataScopeVo, pmFlag);
        if(pmFlag==true) {
            leaveApprovalCount = pmLeaveApprovalDao.countLeaveApproval(approvalStatus);
        }
        else leaveApprovalCount = 0;
        if (leaveApprovalCount < COUNT) {
            leaveApprovalsCharge = pmLeaveApprovalDao.listALLByStatus(approvalStatus);
        }
        return leaveApprovalsCharge;
    }

    private void setpmLeaveApprovalQueryCriteriaCharge(String approvalStatus, PmLeaveApprovalQueryCriteria pmLeaveApprovalQueryCriteriaCharge, FndDataScopeVo dataScopeVo, Boolean pmFlag) {
        pmLeaveApprovalQueryCriteriaCharge.setEnabledFlag(true);
        if (!pmFlag) {
            pmLeaveApprovalQueryCriteriaCharge.setDeptIds(dataScopeVo.getDeptIds());
            pmLeaveApprovalQueryCriteriaCharge.setEmployeeId(dataScopeVo.getEmployeeId());
        }
    }


    //获取员工体检申请情况
    private int getPmMedicalApprovals(Boolean submitFlag, PmMedicalQueryCriteria pmMedicalApprovalQueryCriteriaCharge, int medicalApprovalsCharge, FndDataScopeVo dataScopeVo, Boolean pmFlag,Boolean doctorFlag) {
        pmMedicalApprovalQueryCriteriaCharge.setEnabledFlag(true);
        if (!pmFlag) {
            pmMedicalApprovalQueryCriteriaCharge.setDeptIds(dataScopeVo.getDeptIds());
            pmMedicalApprovalQueryCriteriaCharge.setEmployeeId(dataScopeVo.getEmployeeId());
            if(doctorFlag)
            {
                pmMedicalApprovalQueryCriteriaCharge.setDeptIds(null);
                pmMedicalApprovalQueryCriteriaCharge.setEmployeeId(null);
            }
            medicalApprovalCount = pmMedicalDao.countMedicalApproval(pmMedicalApprovalQueryCriteriaCharge);
        }
        else medicalApprovalCount = 0;
        return medicalApprovalCount;
    }


//    private void setpmMedicalApprovalQueryCriteriaCharge(String approvalStatus, PmMedicalQueryCriteria pmMedicalApprovalsQueryCriteriaCharge, FndDataScopeVo dataScopeVo, Boolean pmFlag) {
//        pmMedicalApprovalsQueryCriteriaCharge.setEnabledFlag(true);
//        if (!pmFlag) {
//            pmMedicalApprovalsQueryCriteriaCharge.setDeptIds(dataScopeVo.getDeptIds());
//            pmMedicalApprovalsQueryCriteriaCharge.setEmployeeId(dataScopeVo.getEmployeeId());
//        }
//    }

    private void setPejQueryCriteria(LocalDate pmTransferDay, PmEmployeeJobTransferQueryCriteria pejQueryCriteria, FndDataScopeVo dataScopeVo, Boolean pmFlag) {
        pejQueryCriteria.setTaskTransferEndTime(pmTransferDay);
        pejQueryCriteria.setEnabled(true);
        pejQueryCriteria.setLeaveFlag(false);
        pejQueryCriteria.setEnabledFlag(true);
        if (!pmFlag) {
            pejQueryCriteria.setDeptIds(dataScopeVo.getDeptIds());
            pejQueryCriteria.setEmployeeId(dataScopeVo.getEmployeeId());
        }
    }

    private List<PmEmployeeEntry> getPmEmployeeEntries(LocalDate entryEndDay, PmEmployeeEntryQueryCriteria entryQueryCriteria, List<PmEmployeeEntry> employeeEntryList, FndDataScopeVo dataScopeVo, Boolean pmFlag) {
        setEntryQueryCriteria(entryEndDay, entryQueryCriteria, dataScopeVo, pmFlag);
        entryCount = pmEmployeeEntryDao.countEntry(entryQueryCriteria);
        if (entryCount < COUNT) {
            employeeEntryList = pmEmployeeEntryDao.listAllByCriteria(entryQueryCriteria);
        }
        return employeeEntryList;
    }

    private void setEntryQueryCriteria(LocalDate entryEndDay, PmEmployeeEntryQueryCriteria entryQueryCriteria, FndDataScopeVo dataScopeVo, Boolean pmFlag) {
        entryQueryCriteria.setSelectTime(entryEndDay);
        entryQueryCriteria.setSelectTimeType("dayAfter");
        entryQueryCriteria.setEnabled(true);
        entryQueryCriteria.setEnabledFlag(true);
        entryQueryCriteria.setLeaveFlag(false);
        if (!pmFlag) {
            entryQueryCriteria.setDeptIds(dataScopeVo.getDeptIds());
            entryQueryCriteria.setEmployeeId(dataScopeVo.getEmployeeId());
        }
    }

    private List<PmEmployeeContract> getPmEmployeeContracts(LocalDate contractEndDay, PmEmployeeContractQueryCriteria pmEmployeeContractQueryCriteriaCharge, List<PmEmployeeContract> pmEmployeeContractListCharge,
                                                            FndDataScopeVo fndDataScopeVo,Boolean pmFlag) {
        // 有没有必要排除自己， 现在是没有排除的
        pmEmployeeContractQueryCriteriaCharge.setToday(LocalDate.now());
        pmEmployeeContractQueryCriteriaCharge.setSelectTime(contractEndDay);
        pmEmployeeContractQueryCriteriaCharge.setEnabled(true);
        pmEmployeeContractQueryCriteriaCharge.setEnabledFlag(true);
        pmEmployeeContractQueryCriteriaCharge.setLeaveFlag(false);
        if (!pmFlag) {
            pmEmployeeContractQueryCriteriaCharge.setDeptIds(fndDataScopeVo.getDeptIds());
            pmEmployeeContractQueryCriteriaCharge.setEmployeeId(fndDataScopeVo.getEmployeeId());
        }
        contractCount = pmEmployeeContractDao.countContract(pmEmployeeContractQueryCriteriaCharge);
        if (contractCount < COUNT) {
            pmEmployeeContractListCharge = pmEmployeeContractDao.listAllByCriteria(pmEmployeeContractQueryCriteriaCharge);
        }
        return pmEmployeeContractListCharge;
    }

    private Long generateAcAbnormal(DateTimeFormatter fmt, Long count, List<FndAgencyVo> targetMenu, List<AcAttendanceRecordHistory> acAttendanceRecordHistoriesCharge, Long acPersonCount, String type ) {
        for (AcAttendanceRecordHistory ac : acAttendanceRecordHistoriesCharge
        ) {
            FndAgencyVo acFndVoPerson = new FndAgencyVo("姓名:" + ac.getEmployee().getName() + ",工牌号:" + ac.getEmployee().getWorkCard() +
                    ",异常日期:" + ac.getDate().format(fmt), type.equals("write") ? AC_HISTORY_WRITE_PATH : AC_HISTORY_DES_APPRIVAL,
                    "{ \"acHisId\": " + ac.getId() + "}", null, count, acPersonCount, null);
            count++;
            targetMenu.add(acFndVoPerson);

        }
        return count;
    }

    // 获取个人合同到期提醒
    private List<PmEmployeeContract> getPersonPmEmployeeContracts(FndUserDTO user, LocalDate contractEndDay) {
        PmEmployeeContractQueryCriteria pmEmployeeContractQueryCriteria = new PmEmployeeContractQueryCriteria();
        pmEmployeeContractQueryCriteria.setEmployeeId(user.getEmployee().getId());
        pmEmployeeContractQueryCriteria.setToday(LocalDate.now());
        pmEmployeeContractQueryCriteria.setSelectTime(contractEndDay);
        pmEmployeeContractQueryCriteria.setEnabled(true);
        return pmEmployeeContractDao.listAllByCriteria(pmEmployeeContractQueryCriteria);
    }

    // 获取个人试用期快到期提醒
    private List<PmEmployeeEntry> getPersonPmEmployeeEntries(FndUserDTO user, LocalDate entryEndDay) {
        PmEmployeeEntryQueryCriteria pmEmployeeEntryQueryCriteria = new PmEmployeeEntryQueryCriteria();
        pmEmployeeEntryQueryCriteria.setSelectTime(entryEndDay);
        pmEmployeeEntryQueryCriteria.setSelectTimeType("dayAfter");
        pmEmployeeEntryQueryCriteria.setEmployeeId(user.getEmployee().getId());
        pmEmployeeEntryQueryCriteria.setEnabled(true);
        return pmEmployeeEntryDao.listAllByCriteria(pmEmployeeEntryQueryCriteria);
    }

    // 获取个人调动
    private List<PmEmployeeJobTransfer> getPersonPmEmployeeJobTransfers(FndUserDTO user, LocalDate pmTransferDay) {
        PmEmployeeJobTransferQueryCriteria pmEmployeeJobTransferQueryCriteria = new PmEmployeeJobTransferQueryCriteria();
        pmEmployeeJobTransferQueryCriteria.setEmployeeId(user.getEmployee().getId());
        pmEmployeeJobTransferQueryCriteria.setTaskTransferEndTime(pmTransferDay);
        pmEmployeeJobTransferQueryCriteria.setEnabled(true);
        return pmEmployeeJobTransferDao.listAllByCriteria(pmEmployeeJobTransferQueryCriteria);
    }

    /**
     *  @author：liangjw
     *  @Date: 2021/2/23 14:48
     *  @Description:  创建个人层
     *  @params:
     *  fmt、fmtDatetime时间转换格式
     *  count 计数标记
     *  targetMenu 菜单
     *  user 当前登录用户信息
     *  acAttendanceRecordHistoryQueryCriteria 个人考勤异常Criteria
     *  PM_CONTRACT_DES 个人待办-合同描述
     *  pmEmployeeContracts 个人待办-快到期合同集合
     *  PM_ENTRY_DES 个人待办-试用期描述
     *  pmEmployeeEntries 个人待办-试用期快到期集合
     *  PM_TRANSFER_DES 个人待办-调动描述
     *  pmEmployeeJobTransfers 个人待办-调动快到期集合
     */
    private Long createPerson(DateTimeFormatter fmt, Long count, List<FndAgencyVo> targetMenu, FndUserDTO user, AcAttendanceRecordHistoryQueryCriteria acAttendanceRecordHistoryQueryCriteria,
                              String PM_CONTRACT_DES, List<PmEmployeeContract> pmEmployeeContracts, String PM_ENTRY_DES, List<PmEmployeeEntry> pmEmployeeEntries, String PM_TRANSFER_DES, List<PmEmployeeJobTransfer> pmEmployeeJobTransfers) {
        if (acHisWritePersonCount > 0 || pmEmployeeContracts.size() > 0
                || pmEmployeeEntries.size() > 0 || pmEmployeeJobTransfers.size() > 0) {
            // 创建个人层
            FndAgencyVo fndAgencyVo = new FndAgencyVo(PERSON_DES, "", "", null, 1L, 0L, null);
            targetMenu.add(fndAgencyVo);
            // 创建异常层
            FndAgencyVo acHistoryVoPerson = new FndAgencyVo(AC_HISTORY_DES_WRITE, AC_HISTORY_WRITE_PATH, "{ \"empId\": " + user.getEmployee().getId() + "}",
                    null, count, 1L, (long)acHisWritePersonCount);
            Long acPersonCount = count;
            targetMenu.add(acHistoryVoPerson);
            count++;
            if (acHisWritePersonCount < COUNT) {
                // 创建异常父子层
                count = generateAcAbnormal(fmt, count, targetMenu, acAttendanceRecordHistoryDao.inUsedListForBatch(acAttendanceRecordHistoryQueryCriteria), acPersonCount, "write");
            }
            // 创建合同层
            FndAgencyVo pmContractAgencyVo = new FndAgencyVo(PM_CONTRACT_DES, null, "{ \"empId\": " + user.getEmployee().getId() + "}",
                    null, count, 1L, (long) pmEmployeeContracts.size());
            targetMenu.add(pmContractAgencyVo);
            Long pmContract = count;
            count++;
            if (pmEmployeeContracts.size() > 0) {
                // 创建合同父子层
                for (PmEmployeeContract pmC : pmEmployeeContracts
                ) {
                    FndAgencyVo pmContractVo = new FndAgencyVo("合同结束日期:" + pmC.getEndTime().format(fmt), null,
                            "{ \"empId\": " + user.getEmployee().getId() + ",\"contractId\":" + pmC.getId() + "}",
                            null, count, pmContract, null);
                    targetMenu.add(pmContractVo);
                    count++;
                }
            }
            // 创建试用期层
            FndAgencyVo pmEntryAgencyVo = new FndAgencyVo(PM_ENTRY_DES, null, "{ \"empId\": " + user.getEmployee().getId() + "}", null,
                    count, 1L, (long) pmEmployeeEntries.size());
            targetMenu.add(pmEntryAgencyVo);
            Long pmEntry = count;
            count++;
            if (pmEmployeeEntries.size() > 0) {
                // 创建试用父子层
                for (PmEmployeeEntry pee : pmEmployeeEntries
                ) {
                    FndAgencyVo pmEntryVo = new FndAgencyVo("试用期结束日期:" + pee.getFormalTime().format(fmt), null,
                            "{ \"empId\": " + user.getEmployee().getId() + ",\"entryId\":" + pee.getId() + "}", null, count, pmEntry, null);
                    targetMenu.add(pmEntryVo);
                    count++;
                }
            }
            // 创建调动层
            FndAgencyVo pmJobTransferVo = new FndAgencyVo(PM_TRANSFER_DES, null, "{ \"empId\": " + user.getEmployee().getId() + "}", null,
                    count, 1L, (long) pmEmployeeJobTransfers.size());
            targetMenu.add(pmJobTransferVo);
            Long jobTransfer = count;
            count++;
            if (pmEmployeeJobTransfers.size() > 0) {
                // 创建调动父子层
                for (PmEmployeeJobTransfer pej : pmEmployeeJobTransfers
                ) {
                    FndAgencyVo jobTransferVo = new FndAgencyVo("调动结束日期:" + pej.getEndTime().format(fmt), null,
                            "{ \"empId\": " + user.getEmployee().getId() + ",\"entryId\":" + pej.getId() + "}", null, count, jobTransfer, null);
                    targetMenu.add(jobTransferVo);
                    count++;
                }
            }
        } else  {
            // 没有数据，创建固定个人层，否则创建个人层
            FndAgencyVo fndAgencyVo = new FndAgencyVo(PERSON_DES, "", "", null, 1L, 0L, null);
            targetMenu.add(fndAgencyVo);
            FndAgencyVo fndAgencyVo1 = new FndAgencyVo("暂无个人待办", null, null, null, count, 1L, null);
            targetMenu.add(fndAgencyVo1);
            count++;
        }
        return count;
    }




}
