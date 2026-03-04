package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.dao.AcAttendanceRecordHistoryDao;
import com.sunten.hrms.ac.dao.AcEmpDeptsDao;
import com.sunten.hrms.ac.domain.AcAttendanceRecordHistory;
import com.sunten.hrms.ac.domain.AcEmpDepts;
import com.sunten.hrms.ac.dto.AcAttendanceRecordHistoryQueryCriteria;
import com.sunten.hrms.ac.dto.AcEmpDeptsQueryCriteria;
import com.sunten.hrms.ac.service.AcAttendanceRecordHistoryService;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.dao.FndAuthorizationDtsDao;
import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.dto.FndDeptQueryCriteria;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.utils.DateUtil;
import com.sunten.hrms.utils.SecurityUtils;
import com.sunten.hrms.utils.ThrowableUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 考勤处理记录历史表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@RestController
@Api(tags = "考勤处理记录历史表")
@RequestMapping("/api/ac/attendanceRecordHistory")
public class AcAttendanceRecordHistoryController {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String ENTITY_NAME = "attendanceRecordHistory";
    private final AcAttendanceRecordHistoryService acAttendanceRecordHistoryService;
    private final AcAttendanceRecordHistoryDao acAttendanceRecordHistoryDao;
    private final FndDataScope fndDataScope;
    private final FndUserService fndUserService;
    private final JwtPermissionService jwtPermissionService;
    private final FndDeptService fndDeptService;
    private final AcEmpDeptsDao acEmpDeptsDao;
    private final FndDeptDao fndDeptDao;
    private final FndAuthorizationDtsDao fndAuthorizationDtsDao;
    @Value("${role.authDocumenter}")
    private String authDocumenter;
    @Value("${role.authRegime}")
    private String authRegime;
    @Value("${role.authTeam}")
    private String authTeam;

    public AcAttendanceRecordHistoryController(AcAttendanceRecordHistoryService acAttendanceRecordHistoryService, FndDataScope fndDataScope,
                                               FndUserService fndUserService, JwtPermissionService jwtPermissionService, FndDeptService fndDeptService,
                                               AcEmpDeptsDao acEmpDeptsDao,
                                               AcAttendanceRecordHistoryDao acAttendanceRecordHistoryDao, FndDeptDao fndDeptDao,
                                               FndAuthorizationDtsDao fndAuthorizationDtsDao) {
        this.acAttendanceRecordHistoryService = acAttendanceRecordHistoryService;
        this.fndDataScope = fndDataScope;
        this.fndUserService = fndUserService;
        this.jwtPermissionService = jwtPermissionService;
        this.fndDeptService = fndDeptService;
        this.acAttendanceRecordHistoryDao = acAttendanceRecordHistoryDao;
        this.acEmpDeptsDao = acEmpDeptsDao;
        this.fndDeptDao = fndDeptDao;
        this.fndAuthorizationDtsDao = fndAuthorizationDtsDao;
    }

    @Log("新增考勤处理记录历史表")
    @ApiOperation("新增考勤处理记录历史表")
    @PostMapping
    @PreAuthorize("@el.check('attendanceRecordHistory:add')")
    public ResponseEntity create(@Validated @RequestBody AcAttendanceRecordHistory attendanceRecordHistory) {
        if (attendanceRecordHistory.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acAttendanceRecordHistoryService.insert(attendanceRecordHistory), HttpStatus.CREATED);
    }

    @Log("删除考勤处理记录历史表")
    @ApiOperation("删除考勤处理记录历史表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('attendanceRecordHistory:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acAttendanceRecordHistoryService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该考勤处理记录历史表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改考勤处理记录历史表")
    @ApiOperation("修改考勤处理记录历史表")
    @PutMapping
    @PreAuthorize("@el.check('attendanceRecordHistory:edit')")
    public ResponseEntity update(@Validated(AcAttendanceRecordHistory.Update.class) @RequestBody AcAttendanceRecordHistory attendanceRecordHistory) {
        acAttendanceRecordHistoryService.update(attendanceRecordHistory);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个考勤处理记录历史表")
    @ApiOperation("获取单个考勤处理记录历史表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public ResponseEntity getAttendanceRecordHistory(@PathVariable Long id) {
        return new ResponseEntity<>(acAttendanceRecordHistoryService.getByKey(id), HttpStatus.OK);
    }


    @ErrorLog("导出考勤处理记录历史表数据")
    @ApiOperation("导出考勤处理记录历史表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public void download(HttpServletResponse response, AcAttendanceRecordHistoryQueryCriteria criteria) throws IOException {
        acAttendanceRecordHistoryService.download(acAttendanceRecordHistoryService.listAll(criteria), response);
    }

    @ErrorLog("导出考勤处理记录历史表数据")
    @ApiOperation("导出考勤处理记录历史表数据")
    @GetMapping(value = "/downloadAcAdmin")
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public void downloadAcAdmin(HttpServletResponse response, AcAttendanceRecordHistoryQueryCriteria criteria) throws IOException {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        setTimeMethod(criteria);
        setCriteriaByRole(criteria, user, "watch");
        acAttendanceRecordHistoryService.downloadAcAdmin(acAttendanceRecordHistoryService.inUsedListForBatch(criteria), response);
    }


    @Log("批量异常处理")
    @ApiOperation("批量异常处理")
    @PutMapping(value = "/batchSort")
    @PreAuthorize("@el.check('exceptionDispose:edit')")
    public ResponseEntity batchSort(@RequestBody List<AcAttendanceRecordHistory> acAttendanceRecordHistories) {
        acAttendanceRecordHistoryService.bartchDispose(acAttendanceRecordHistories);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("批量处理前置查询")
    @ApiOperation("批量处理前置查询")
    @PutMapping(value = "/listForBatch")
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public ResponseEntity listForBatch(@RequestBody AcAttendanceRecordHistoryQueryCriteria criteria) {
        return new ResponseEntity<>(acAttendanceRecordHistoryService.inUsedListForBatch(criteria), HttpStatus.OK);
    }

//    @ErrorLog("代办列表查询")
//    @ApiOperation("代办列表查询")
//    @GetMapping(value = "/getListByUser")
//    public ResponseEntity getListByUser() {
//        AcAttendanceRecordHistoryQueryCriteria acAttendanceRecordHistoryQueryCriteria = new AcAttendanceRecordHistoryQueryCriteria();
//        acAttendanceRecordHistoryQueryCriteria.setAbNormalFlag(true);
//        acAttendanceRecordHistoryQueryCriteria.setDisposeFlag("not");
//        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
//        acAttendanceRecordHistoryQueryCriteria.setEmployeeId(user.getEmployee().getId());
//        return new ResponseEntity<>(acAttendanceRecordHistoryService.listAll(acAttendanceRecordHistoryQueryCriteria), HttpStatus.OK);
//    }
//


    @ErrorLog("考勤异常填写分页查询")
    @ApiOperation("考勤异常填写分页查询")
    @GetMapping(value = "/listForExceptionWrite")
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public ResponseEntity listForExceptionWrite(AcAttendanceRecordHistoryQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) { // 资料员也放在这一级
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        setTimeMethod(criteria);
        setCriteriaByRole(criteria, user, "write");
        setDeptIdsByPageType(criteria, "write");
        return new ResponseEntity<>(acAttendanceRecordHistoryService.inUsedListForBatchByPage(pageable, criteria), HttpStatus.OK);
    }

    private void setDeptIdsByPageType(AcAttendanceRecordHistoryQueryCriteria criteria, String pageType) {
        if (null != criteria.getDeptId()) {
            // 原部门范围下的节点架构
            Set<Long> deptIds = new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId()));
            logger.debug("原部门范围下的节点架构:" + deptIds);
            // 根据权限出的部门节点
            Set<Long> authDeptIds = this.getDeptListByAllRole(pageType, deptIds);
            logger.debug("根据权限范围出的节点:" + authDeptIds);
            //取交集
            Set<Long> targetDeptIds = new HashSet<>(deptIds);
            targetDeptIds.retainAll(authDeptIds);
            logger.debug("交集结果:" + targetDeptIds);
            criteria.setDeptIds(targetDeptIds);
        }
    }


    @ErrorLog("考勤异常审批分页查询")
    @ApiOperation("考勤异常审批分页查询")
    @GetMapping(value = "/listForExceptionApproval")
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public ResponseEntity listForExceptionApproval(AcAttendanceRecordHistoryQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        setTimeMethod(criteria);
        setCriteriaByRole(criteria, user, "approval");
        setDeptIdsByPageType(criteria, "approval");
        return new ResponseEntity<>(acAttendanceRecordHistoryService.inUsedListForBatchByPage(pageable, criteria), HttpStatus.OK);
    }

    private void setTimeMethod(AcAttendanceRecordHistoryQueryCriteria criteria) {
        if (criteria.getBeginDate() != null) {
            criteria.setBeginDateStr(DateUtil.localDateToStr(criteria.getBeginDate()));
            criteria.setBeginDate(null);
        }
        if (criteria.getEndDate() != null) {
            criteria.setEndDateStr(DateUtil.localDateToStr(criteria.getEndDate()));
            criteria.setEndDate(null);
        }
    }

    @ErrorLog("返回当前登录人的empId")
    @ApiOperation("返回当前登录人的empId")
    @GetMapping(value = "/getEmpId")
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public ResponseEntity getEmpId() {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        return new ResponseEntity<>(user.getEmployee().getId(), HttpStatus.OK);
    }

    @ErrorLog("考勤异常分页查询")
    @ApiOperation("考勤异常分页查询")
    @GetMapping(value = "/listForExceptionByPage")
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public ResponseEntity listForExceptionByPage(AcAttendanceRecordHistoryQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        setTimeMethod(criteria);
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        setCriteriaByRole(criteria, user, "watch");
        return new ResponseEntity<>(acAttendanceRecordHistoryService.inUsedListForBatchByPage(pageable, criteria), HttpStatus.OK);

    }

    @ErrorLog("考勤异常上月提交前置检查")
    @ApiOperation("考勤异常上月提交前置检查")
    @GetMapping(value = "/checkBeforeUpdateCommitFlagByMonth")
    public ResponseEntity checkBeforeUpdateCommitFlagByMonth() {
        return batchCommitMethod(true);
        //        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        //        if (null == user.getEmployee().getId()) {
        //            throw new InfoCheckWarningMessException("该用户没有绑定员工，请联系管理员");
        //        }
        //        LocalDate now = LocalDate.now();
        //        LocalDate startDate = now.getMonthValue() == 1 ? LocalDate.of(now.getYear() - 1, 12, 1)
        //                : LocalDate.of(now.getYear(), now.getMonthValue() - 1, 1);
        //        LocalDate endDate = now.getMonthValue() == 1
        //                ? LocalDate.of(now.getYear() - 1, 12, LocalDate.of(now.getYear() - 1, 12, 1).lengthOfMonth())
        //                : LocalDate.of(now.getYear(), now.getMonthValue() - 1, LocalDate.of(now.getYear(), now.getMonthValue() - 1, 1).lengthOfMonth());
        //        AcEmpDeptsQueryCriteria acEmpDeptsQueryCriteria = new AcEmpDeptsQueryCriteria();
        //        acEmpDeptsQueryCriteria.setRoleId(17L);
        //        acEmpDeptsQueryCriteria.setDataType(authDocumenter);
        //        acEmpDeptsQueryCriteria.setEmployeeId(user.getEmployee().getId());
        //        acEmpDeptsQueryCriteria.setEnabledFlag(true);
        //        List<AcEmpDepts> acEmpDeptsList = acEmpDeptsDao.listAllByCriteria(acEmpDeptsQueryCriteria);
        //        Set<Long> acDeptIds = acEmpDeptsList.stream().map(AcEmpDepts::getDeptId).collect(Collectors.toSet());
        //        if (acDeptIds.size() <= 0) {
        //            throw new InfoCheckWarningMessException("没检测到该用户有资料员权限");
        //        }
        //        AcAttendanceRecordHistoryQueryCriteria acAttendanceRecordHistoryQueryCriteria = new AcAttendanceRecordHistoryQueryCriteria();
        //        setLastMonthAttendanceRecordQueryCriteria(acAttendanceRecordHistoryQueryCriteria, startDate, endDate, acDeptIds);
        //        //         该月未处理异常数量
        //        acAttendanceRecordHistoryQueryCriteria.setDocCodeStatus("person");
        //        Integer notDisposeCount = acAttendanceRecordHistoryDao.inUsedCountForDocCommit(acAttendanceRecordHistoryQueryCriteria);
        //        if (notDisposeCount > 0) {
        //            return new ResponseEntity<>("error", HttpStatus.OK);
        //        } else {
        //            acAttendanceRecordHistoryQueryCriteria.setDocCodeStatus("documentor");
        //            // 该月复核中的异常数量
        //            Integer checkCount = acAttendanceRecordHistoryDao.inUsedCountForDocCommit(acAttendanceRecordHistoryQueryCriteria);
        //            if (checkCount > 0) {
        //                return new ResponseEntity<>("warning", HttpStatus.OK);
        //            } else {
        //                return new ResponseEntity<>("success", HttpStatus.OK);
        //            }
        //        }
    }

    // 这个就由前台控制好按钮权限， 后台这里不好做
    @Log("考勤异常上月提交")
    @ApiOperation("考勤异常上月提交")
    @PutMapping(value = "/updateCommitFlagByMonth")
    public ResponseEntity updateCommitFlagByMonth() {
        // 先判定能不能提
        //        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        //        if (null == user.getEmployee().getId()) {
        //            throw new InfoCheckWarningMessException("该用户没有绑定员工，请联系管理员");
        //        }
        //        LocalDate now = LocalDate.now();
        //        LocalDate startDate = now.getMonthValue() == 1 ? LocalDate.of(now.getYear() - 1, 12, 1)
        //                : LocalDate.of(now.getYear(), now.getMonthValue() - 1, 1);
        //        LocalDate endDate = now.getMonthValue() == 1
        //                ? LocalDate.of(now.getYear() - 1, 12, LocalDate.of(now.getYear() - 1, 12, 1).lengthOfMonth())
        //                : LocalDate.of(now.getYear(), now.getMonthValue() - 1, LocalDate.of(now.getYear(), now.getMonthValue() - 1, 1).lengthOfMonth());
        //        // 根据user获取资料员范围
        //        AcEmpDeptsQueryCriteria acEmpDeptsQueryCriteria = new AcEmpDeptsQueryCriteria();
        //        acEmpDeptsQueryCriteria.setRoleId(17L);
        //        acEmpDeptsQueryCriteria.setDataType(authDocumenter);
        //        acEmpDeptsQueryCriteria.setEmployeeId(user.getEmployee().getId());
        //        acEmpDeptsQueryCriteria.setEnabledFlag(true);
        //        List<AcEmpDepts> acEmpDeptsList = acEmpDeptsDao.listAllByCriteria(acEmpDeptsQueryCriteria);
        //        Set<Long> acDeptIds = acEmpDeptsList.stream().map(AcEmpDepts::getDeptId).collect(Collectors.toSet());
        //        if (acDeptIds.size() <= 0) {
        //            throw new InfoCheckWarningMessException("没检测到该用户有资料员权限");
        //        }
        //
        //        // 上月目标数量
        //        AcAttendanceRecordHistoryQueryCriteria acAttendanceRecordHistoryQueryCriteria = new AcAttendanceRecordHistoryQueryCriteria();
        //        setLastMonthAttendanceRecordQueryCriteria(acAttendanceRecordHistoryQueryCriteria, startDate, endDate, acDeptIds);
        //        // 该月所有异常数量
        //        acAttendanceRecordHistoryQueryCriteria.setDocBatchCommitCodeStatusFlag(true); // 复核中及未提交
        //        // 批量更新
        //        acAttendanceRecordHistoryQueryCriteria.setBeginDateStr(null);
        //        acAttendanceRecordHistoryQueryCriteria.setEndDateStr(null);
        //        acAttendanceRecordHistoryQueryCriteria.setEndAndBeforeStr(DateUtil.localDateToStr(endDate));
        //        acAttendanceRecordHistoryService.batchCommit(acAttendanceRecordHistoryQueryCriteria, acEmpDeptsList, user);
        //        return new ResponseEntity(HttpStatus.OK);
        return batchCommitMethod(false);
    }

    /**
     * @author：liangjw
     * @Date: 2021/7/22 8:56
     * @Description: 批量提交上月异常以及批量提交上月异常的前置数数方法
     * @params: checkBeforeFlag 为true，前置数数； checkBeforeFlag为false， 提交上月异常
     */
    private ResponseEntity batchCommitMethod(Boolean checkBeforeFlag) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        if (null == user.getEmployee().getId()) {
            throw new InfoCheckWarningMessException("该用户没有绑定员工，请联系管理员");
        }
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.getMonthValue() == 1 ? LocalDate.of(now.getYear() - 1, 12, 1)
                : LocalDate.of(now.getYear(), now.getMonthValue() - 1, 1);
        LocalDate endDate = now.getMonthValue() == 1
                ? LocalDate.of(now.getYear() - 1, 12, LocalDate.of(now.getYear() - 1, 12, 1).lengthOfMonth())
                : LocalDate.of(now.getYear(), now.getMonthValue() - 1, LocalDate.of(now.getYear(), now.getMonthValue() - 1, 1).lengthOfMonth());
        // 根据user获取资料员范围
        AcEmpDeptsQueryCriteria acEmpDeptsQueryCriteria = new AcEmpDeptsQueryCriteria();
        acEmpDeptsQueryCriteria.setRoleId(17L);
        acEmpDeptsQueryCriteria.setDataType(authDocumenter);
        acEmpDeptsQueryCriteria.setEmployeeId(user.getEmployee().getId());
        acEmpDeptsQueryCriteria.setEnabledFlag(true);
        List<AcEmpDepts> acEmpDeptsList = acEmpDeptsDao.listAllByCriteria(acEmpDeptsQueryCriteria);
        Set<Long> acDeptIds = acEmpDeptsList.stream().map(AcEmpDepts::getDeptId).collect(Collectors.toSet());
        if (acDeptIds.size() <= 0) {
            throw new InfoCheckWarningMessException("没检测到该用户有资料员权限");
        }
        if (checkBeforeFlag) {
            AcAttendanceRecordHistoryQueryCriteria acAttendanceRecordHistoryQueryCriteria = new AcAttendanceRecordHistoryQueryCriteria();
            setLastMonthAttendanceRecordQueryCriteria(acAttendanceRecordHistoryQueryCriteria, startDate, endDate, acDeptIds);
            // 该月未处理异常数量
            acAttendanceRecordHistoryQueryCriteria.setDocCodeStatus("person");
            Integer notDisposeCount = acAttendanceRecordHistoryDao.inUsedCountForDocCommit(acAttendanceRecordHistoryQueryCriteria);
            if (notDisposeCount > 0) {
                return new ResponseEntity<>("error", HttpStatus.OK);
            } else {
                acAttendanceRecordHistoryQueryCriteria.setDocCodeStatus("documentor");
                // 该月复核中的异常数量
                Integer checkCount = acAttendanceRecordHistoryDao.inUsedCountForDocCommit(acAttendanceRecordHistoryQueryCriteria);
                if (checkCount > 0) {
                    return new ResponseEntity<>("warning", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("success", HttpStatus.OK);
                }
            }
        } else {
            // 上月目标数量
            AcAttendanceRecordHistoryQueryCriteria acAttendanceRecordHistoryQueryCriteria = new AcAttendanceRecordHistoryQueryCriteria();
            setLastMonthAttendanceRecordQueryCriteria(acAttendanceRecordHistoryQueryCriteria, startDate, endDate, acDeptIds);
            // 该月所有异常数量
            acAttendanceRecordHistoryQueryCriteria.setDocBatchCommitCodeStatusFlag(true); // 复核中及未提交
            // 批量更新
            acAttendanceRecordHistoryQueryCriteria.setBeginDateStr(null);
            acAttendanceRecordHistoryQueryCriteria.setEndDateStr(null);
            acAttendanceRecordHistoryQueryCriteria.setEndAndBeforeStr(DateUtil.localDateToStr(endDate));
            acAttendanceRecordHistoryService.batchCommit(acAttendanceRecordHistoryQueryCriteria, acEmpDeptsList, user);
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    private void setLastMonthAttendanceRecordQueryCriteria(AcAttendanceRecordHistoryQueryCriteria acAttendanceRecordHistoryQueryCriteria, LocalDate startDate, LocalDate endDate, Set<Long> acDeptIds) {
        acAttendanceRecordHistoryQueryCriteria.setBeginDate(startDate);
        acAttendanceRecordHistoryQueryCriteria.setEndDate(endDate);
        setTimeMethod(acAttendanceRecordHistoryQueryCriteria);
        acAttendanceRecordHistoryQueryCriteria.setDocUnion(true);
        acAttendanceRecordHistoryQueryCriteria.setDocCommitFlag(false);
        acAttendanceRecordHistoryQueryCriteria.setDocDeptIds(acDeptIds);
        acAttendanceRecordHistoryQueryCriteria.setDocRemoveFlag(true);
    }

    /**
     * @author：liangjw
     * @Date: 2021/4/20 10:02
     * @Description: 根据班组长、资料员、授权范围、自身权限范围获取部门id集合
     * @params: 页面类别
     */
    private Set<Long> getDeptListByAllRole(String pageType, Set<Long> originalDeptIds) {
        boolean regimeFlag = false;
        FndDeptQueryCriteria fndDeptQueryCriteria = new FndDeptQueryCriteria();
        fndDeptQueryCriteria.setEnabled(true);
        fndDeptQueryCriteria.setDeleted(false);
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Set<Long> targetDeptIds = new HashSet<>();
        // 考勤管理员
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority auth : grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals(authRegime)) {
                // 返回全部节点
                regimeFlag = true;
            }
        }
        // 班长组长范围
//         AcEmpDeptsQueryCriteria acEmpDeptsQueryCriteria = getAcEmpDeptsQueryCriteria(user);
        AcEmpDeptsQueryCriteria acEmpDeptsQueryCriteria = new AcEmpDeptsQueryCriteria();
        acEmpDeptsQueryCriteria.setDataType(authTeam);
        acEmpDeptsQueryCriteria.setEnabledFlag(true);
        acEmpDeptsQueryCriteria.setEmployeeId(user.getEmployee().getId());
//         acEmpDeptsQueryCriteria.setDataType(authTeam);
        Set<Long> deptIds = acEmpDeptsDao.listAllByCriteria(acEmpDeptsQueryCriteria).stream().map(AcEmpDepts::getDeptId).collect(Collectors.toSet());
        acEmpDeptsQueryCriteria.setDataType(authDocumenter);
        Set<Long> acDeptIds = acEmpDeptsDao.listAllByCriteria(acEmpDeptsQueryCriteria).stream().map(AcEmpDepts::getDeptId).collect(Collectors.toSet());
        // 经理
        fndDeptQueryCriteria.setNoUser(true);
        // 获取管辖范围
        Set<Long> targets = new HashSet<>();
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, false, null, null);
        // 管理岗的范围
        List<FndDept> fndDepCharge = fndDeptDao.getDeptByEmpAndAdminJob(user.getEmployee().getId());

        if (!fndDataScope.isNoDataPermission(dataScopeVo) && null == dataScopeVo.getEmployeeId()) {
            // 这里需要注意的点是，岗位或者角色是的范围是全部的时候，dataScopeVo.getDeptIds 是空集的逻辑，但是这里审批不适合这么用，故全部的数据范围在此处不适用
            // 如果想要适用的话，开放以下注释, 并注释掉原来的target赋值（liangjw注）
            //            if (dataScopeVo.getDeptIds() != null && dataScopeVo.getDeptIds().size() == 0) {
            //                targets = fndDeptService.listAllChildrenByPid(1L).stream().map(FndDept::getId).collect(Collectors.toSet());
            //            } else {
            //                targets = dataScopeVo.getDeptIds();
            //            }
            targets = dataScopeVo.getDeptIds();
        }
        // 补充授权范围
        List<Long> authDeptIds = fndAuthorizationDtsDao.getAuthorizationDeptsByToEmployee(user.getEmployee().getId());
        if (pageType.equals("write")) {
            // 个人、资料员、班长
            targetDeptIds.addAll(deptIds);
            targetDeptIds.addAll(acDeptIds);
            targetDeptIds.add(user.getDept().getId());
            return targetDeptIds;
        }
        targetDeptIds.addAll(authDeptIds);
        if (pageType.equals("approval")) {
            // 个人、管理员、 领导
            if (regimeFlag) {
                targetDeptIds.addAll(originalDeptIds);
                return targetDeptIds;
            } else {
                targetDeptIds.add(user.getDept().getId());
                targetDeptIds.addAll(targets);
                return targetDeptIds;
            }
        }
        if (pageType.equals("watch")) {
            fndDeptQueryCriteria.setNoUser(false);
            if (regimeFlag) {
                return targetDeptIds;
            } else {
                targetDeptIds.add(user.getDept().getId());
                targetDeptIds.addAll(deptIds);
                targetDeptIds.addAll(acDeptIds);
                // 获取管辖范围
                FndDataScopeVo dataScopeVoTemp = new FndDataScopeVo(null, null, false, false, null, null);
                if (!fndDataScope.isNoDataPermission(dataScopeVoTemp) && null == dataScopeVoTemp.getEmployeeId()) {
                    targetDeptIds.addAll(dataScopeVoTemp.getDeptIds());
                }
                for (FndDept fd : fndDepCharge
                ) {
                    targetDeptIds.addAll(fndDeptService.listAllEnableChildrenIdByPid(fd.getId()));
                }
                return targetDeptIds;
            }
        }
        return new HashSet<>();
    }

    private AcEmpDeptsQueryCriteria getAcEmpDeptsQueryCriteria(FndUserDTO user) {
        AcEmpDeptsQueryCriteria acEmpDeptsQueryCriteria = new AcEmpDeptsQueryCriteria();
        acEmpDeptsQueryCriteria.setDataType(authTeam);
        acEmpDeptsQueryCriteria.setEnabledFlag(true);
        acEmpDeptsQueryCriteria.setEmployeeId(user.getEmployee().getId());
        return acEmpDeptsQueryCriteria;
    }

    @ErrorLog("考勤异常专用部门查询树")
    @ApiOperation("考勤异常专用部门查询树")
    @GetMapping(value = "/getExceptionTree")
    @PreAuthorize("@el.check('exceptionDispose:list')")
    public ResponseEntity getExceptionTree(String pageType) {
        FndDeptQueryCriteria fndDeptQueryCriteria = new FndDeptQueryCriteria();
        fndDeptQueryCriteria.setEnabled(true);
        fndDeptQueryCriteria.setDeleted(false);
        boolean regimeFlag = false;
        // 考勤管理员
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority auth : grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals(authRegime)) {
                // 返回全部节点
                regimeFlag = true;
            }
        }
        Set<Long> targetDeptIds = this.getDeptListByAllRole(pageType, new HashSet<>());
        if (pageType.equals("write")) {
            FndDeptQueryCriteria fndDeptQueryCriteriaTemp = new FndDeptQueryCriteria();
            fndDeptQueryCriteriaTemp.setDeleted(false);
            fndDeptQueryCriteriaTemp.setEnabled(true);
            fndDeptQueryCriteriaTemp.setIds(targetDeptIds);
            return new ResponseEntity<>(fndDeptService.buildAcTree(fndDeptService.listAll(fndDeptQueryCriteriaTemp)), HttpStatus.OK);
        }
        if (pageType.equals("approval")) {
            if (regimeFlag) {
                return new ResponseEntity<>(fndDeptService.buildAcTree(fndDeptService.listAll(fndDeptQueryCriteria)), HttpStatus.OK);
            } else {
                logger.debug("考勤异常专用部门查询树: targetDeptIds" + targetDeptIds);
                fndDeptQueryCriteria.setIds(targetDeptIds);
                return new ResponseEntity<>(fndDeptService.buildAcTree(fndDeptService.listAll(fndDeptQueryCriteria)), HttpStatus.OK);
            }
        }
        if (pageType.equals("watch")) {
            fndDeptQueryCriteria.setNoUser(false);
            if (regimeFlag) {
                return new ResponseEntity<>(fndDeptService.buildAcTree(fndDeptService.listAll(fndDeptQueryCriteria)), HttpStatus.OK);
            } else {
                fndDeptQueryCriteria.setIds(targetDeptIds);
                return new ResponseEntity<>(fndDeptService.buildAcTree(fndDeptService.listAll(fndDeptQueryCriteria)), HttpStatus.OK);
            }
        }
        throw new InfoCheckWarningMessException("Ac构成树接口使用错误");
    }

    private void setCriteriaByRole(AcAttendanceRecordHistoryQueryCriteria criteria, FndUserDTO user, String useMethod) {
        boolean chargeFlag = false;
        boolean regimeFlag = false;
        boolean authFlag = false;
        Set<Long> criteriaDeptId = new HashSet<>(); // 用于最外层查询得部门集合
        if (null == user.getEmployee()) {
            throw new InfoCheckWarningMessException("该用户没有绑定员工，请联系管理员");
        }
        // 检查班长站长
        AcEmpDeptsQueryCriteria acEmpDeptsQueryCriteria = getAcEmpDeptsQueryCriteria(user);
        // 检测资料员
//        acEmpDeptsQueryCriteria.setDataType(authTeam);
        Set<Long> deptIds = acEmpDeptsDao.listAllByCriteria(acEmpDeptsQueryCriteria).stream().map(AcEmpDepts::getDeptId).collect(Collectors.toSet());
        boolean teamFlag = isFlag(deptIds);
        acEmpDeptsQueryCriteria.setDataType(authDocumenter);
        Set<Long> acDeptIds = acEmpDeptsDao.listAllByCriteria(acEmpDeptsQueryCriteria).stream().map(AcEmpDepts::getDeptId).collect(Collectors.toSet());

        boolean docFlag = isFlag(acDeptIds);
        // 获取管理岗, 这个可以用来判断是否管理层
        List<FndDept> fndDepCharge = fndDeptDao.getDeptByEmpAndAdminJob(user.getEmployee().getId());
        criteria.setBaseQueryEmpId(user.getEmployee().getId());
        if (useMethod.equals("write")) {
            //开启个人层
            criteria.setPersonUnion(true);
            criteria.setPersonEmployeeId(user.getEmployee().getId());
            if (fndDepCharge.size() > 0) {
                // 领导可以自己填写 以及被打回的
                criteria.setChara("charge");
            } else {
                if (docFlag) { // 资料员
                    criteria.setChara("documentor");
                } else {
                    // 普通人员只能看到要填写的， 打回的只有资料员看见
                    criteria.setChara("person");
                }
            }
            if (teamFlag || docFlag) {
                if (teamFlag && docFlag) {
                    // 开启班长层, 以防万一排除管理岗
                    criteria.setTeamUnion(true);
                    criteria.setTeamDeptIds(deptIds);
                    criteria.setTeamCodeStatus("all");
                    criteria.setTeamRemoveFlag(true);
                    // 开启资料员层排除管理岗
                    criteria.setDocUnion(true);
                    criteria.setDocDeptIds(acDeptIds);
                    criteria.setDocQueryFlag(true); // 开启最主要的资料员查询范围
                    criteria.setDocRemoveFlag(true);
                }
                if (teamFlag) {
                    criteria.setTeamUnion(true);
                    criteria.setTeamDeptIds(deptIds);
                    criteria.setTeamCodeStatus("all");
                    criteria.setTeamRemoveFlag(true);
                }
                // 资料员
                if (docFlag) {
                    criteria.setDocUnion(true);
                    criteria.setDocDeptIds(acDeptIds);
                    criteria.setDocQueryFlag(true);
                    criteria.setDocRemoveFlag(true);
                }
            }
        }
        // 取出部门管理岗上没有任职人员的部门
        List<FndDept> fndDepts = getFndDepts();
        // 存储交集
        List<FndDept> targets;
        // 存储差集
        List<Long> reduceTargets = new ArrayList<>();
        // 最终的管理范围
        List<Long> totalDept = new ArrayList<>();
        // 获取管辖范围
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptId(), null, false, false, criteria.getDeptIds(), criteria.getEmployeeId());
        if (!fndDataScope.isNoDataPermission(dataScopeVo) && null == dataScopeVo.getEmployeeId()) {
            logger.debug("isNoDataPermission出来的depatIds" + dataScopeVo.getDeptIds());
            // 取出交集，即无管理人员且在自身管辖范围内
            // list 排除了自己部门的管辖范围
            List<Long> list = dataScopeVo.getDeptIds().stream().filter(item -> !item.equals(user.getDept().getId())).collect(Collectors.toList());
            // targets 在自己管辖范围内没有管理岗或有管理岗但是没有人的部门
            targets = fndDepts.stream().filter(fndDept -> list.contains(fndDept.getId())).collect(Collectors.toList());
            // 取出差集
            if (list.size() > 0) {
                for (Long i : list
                ) {
                    if (!targets.stream().map(FndDept::getId).collect(Collectors.toList()).contains(i)) {
                        reduceTargets.add(i);
                    }
                }
            }
            // reduceTargets 存储的是有管理岗且在自己管辖范围内
            logger.debug("list" + list);
            logger.debug("targets" + targets);
            logger.debug("reduceTargets" + reduceTargets);
            // 判断 targets中的parentid 在不在差集内，若在则需要排除
            for (FndDept fd : targets
            ) {
                if (!reduceTargets.contains(fd.getParentId())) {
                    totalDept.add(fd.getId());
                }
            }
        }
        logger.debug(dataScopeVo.toString());
        if (totalDept.size() > 0 ||
                fndDepCharge.size() > 0) { // 领导(岗位范围或为管理岗得岗位)  至少有一个管理部门才认定为领导
            chargeFlag = true;
            if (fndDepCharge.size() > 0) {
                totalDept.add(user.getDept().getId());
            }
        }
        // 获取授权部分
        List<Long> authDeptIds = fndAuthorizationDtsDao.getAuthorizationDeptsByToEmployee(user.getEmployee().getId());
        if (authDeptIds.size() > 0) {
            authFlag = true;
        }
        // 考勤专员
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority auth : grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals(authRegime)) {
                regimeFlag = true;
            }
        }
        if (useMethod.equals("watch")) { // 查看
            List<Long> setByDeptId = fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId());
            if (regimeFlag) { // 考勤管理员返回全部
                // 开启班长层以代替
                criteria.setTeamUnion(true);
                criteria.setDeptIds(new HashSet<>(setByDeptId));
            } else {// 领导、 站长 、 资料员 、 个人 合并
                if (teamFlag) {
                    // 开启班长层
                    criteria.setTeamUnion(true);
                    criteria.setTeamRemoveFlag(true);
                    criteria.setTeamDeptIds(deptIds);
                    criteriaDeptId.addAll(deptIds);
                }
                if (docFlag) { // 开启资料员层
                    criteria.setDocUnion(true);
                    criteria.setDocRemoveFlag(true);
                    criteria.setDocDeptIds(acDeptIds);
                    criteriaDeptId.addAll(acDeptIds);
                }
                if (chargeFlag) { // 开启领导层
                    criteria.setChargeUnion(true);
                    Set<Long> watchChargeIds = new HashSet<>();
                    // 除了岗位下挂之外， 还要合并在他管理岗下的所有员工
                    for (FndDept fd : fndDepCharge
                    ) {
                        watchChargeIds.addAll(fndDeptService.listAllEnableChildrenIdByPid(fd.getId()));
                    }
                    watchChargeIds.addAll(dataScopeVo.getDeptIds());
                    criteria.setChargeDeptIds(watchChargeIds);
                    criteriaDeptId.addAll(watchChargeIds);
                }
                // 开启个人层
                criteria.setPersonUnion(true);
                criteria.setPersonEmployeeId(user.getEmployee().getId());
                criteriaDeptId.add(user.getEmployee().getId());
                // 将criteriaDeptId 与 范围取交集, 得到最外层得部门查询集合
                Set<Long> targetCriteriaDeptId = criteriaDeptId.stream().filter(setByDeptId::contains).collect(Collectors.toSet());
                criteria.setDeptIds(targetCriteriaDeptId);
            }
        }
        // 审批 以班长层、资料员层的参数进行代替查询
        if (useMethod.equals("approval")) {
            if (chargeFlag || authFlag) {
                // 领导部分先排除管理岗
                criteria.setTeamUnion(true);
                criteria.setTeamCommitFlag(true);
                criteria.setTeamCodeStatus("leader");
                criteria.setTeamRemoveFlag(true);
                Set<Long> teamDeptIds = new HashSet<>(totalDept);
                logger.debug("dataScopeVo.getDeptIds(), 岗位的部门范围--------------------" + dataScopeVo.getDeptIds());
                logger.debug("user.getDept().getId()" + user.getDept().getId());
                logger.debug("teamDeptIds==========" + teamDeptIds);
                setTeamDeptIdsAndSetDeptsIsNull(criteria, user, authFlag, dataScopeVo, authDeptIds, teamDeptIds);
                logger.debug("teamDeptIds" + teamDeptIds);
                logger.debug("chargeFlag" + chargeFlag);
                if (null != user.getJob().getId()) {
                    FndDeptQueryCriteria tempDeptQuery = new FndDeptQueryCriteria();
                    tempDeptQuery.setAdminJobId(user.getJob().getId());
                    tempDeptQuery.setEnabled(true);
                    teamDeptIds.addAll(fndDeptDao.listAllByCriteria(tempDeptQuery).stream().map(FndDept::getId).collect(Collectors.toList()));
                }
                logger.debug("teamDeptIds" + teamDeptIds);
                criteria.setTeamDeptIds(teamDeptIds);
                // 设置原始的数据范围
                logger.debug("原始的数据范围" + dataScopeVo.getDeptIds());
                criteria.setTeamDsDeptIds(dataScopeVo.getDeptIds());
                // 再合并管理线的管理人员
                if (chargeFlag) {
                    criteria.setTeamLowerLevel(true);
                    criteria.setHigherLevelEmployeeId(user.getEmployee().getId());
                }
                // 再合并授权范围
                if (authDeptIds.size() > 0) {
                    criteria.setTeamAuthLowerLevel(true);
                    criteria.setTeamAuthDeptIds(authDeptIds);
                }
            }
            if (regimeFlag) {
                criteria.setDocCommitFlag(true);
                criteria.setDocUnion(true);
                criteria.setDocCodeStatus("acAdmin");
                criteria.setDocRemoveFlag(true);
            }
            if (!(regimeFlag || chargeFlag || authFlag)) {
                throw new InfoCheckWarningMessException("当前用户无数据可审");
            }
        }
    }

    private boolean isFlag(Set<Long> acDeptIds) {
        return acDeptIds.size() > 0;
    }

    private List<FndDept> getFndDepts() {
        FndDeptQueryCriteria fndDeptQueryCriteria = new FndDeptQueryCriteria();
        fndDeptQueryCriteria.setEnabled(true);
        fndDeptQueryCriteria.setDeleted(false);
        fndDeptQueryCriteria.setNoUser(true);
        return fndDeptDao.listAllByCriteria(fndDeptQueryCriteria);
    }

    /**
     * @author：liangjw
     * @Date: 2021/7/22 9:02
     * @Description: 设置teamDeptIds以及DeptsIsnull等参数
     */
    public static void setTeamDeptIdsAndSetDeptsIsNull(AcAttendanceRecordHistoryQueryCriteria criteria, FndUserDTO user, Boolean authFlag, FndDataScopeVo dataScopeVo, List<Long> authDeptIds, Set<Long> teamDeptIds) {
        if (null != dataScopeVo.getDeptIds() && dataScopeVo.getDeptIds().size() == 1 && user.getDept().getId().equals(new ArrayList<>(dataScopeVo.getDeptIds()).get(0))) {
            teamDeptIds.addAll(dataScopeVo.getDeptIds());
        }
        if (authFlag) {
            teamDeptIds.addAll(authDeptIds);
        }
        if (teamDeptIds.size() <= 0) {
            criteria.setTeamDeptIdsIsNull(true);
        }
    }

    @Log("处理异常记录")
    @ApiOperation("处理异常记录")
    @PutMapping(value = "/disposeAcAttendanceRecordHistory")
    @PreAuthorize("@el.check('exceptionDispose:edit')")
    public ResponseEntity disposeClock(@RequestBody List<AcAttendanceRecordHistory> acAttendanceRecordHistories) {
        acAttendanceRecordHistoryService.disposeRecordHistory(acAttendanceRecordHistories);
        return new ResponseEntity(HttpStatus.OK);
    }
}
