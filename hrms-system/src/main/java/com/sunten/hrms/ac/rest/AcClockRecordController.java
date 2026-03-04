package com.sunten.hrms.ac.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.ac.dao.AcEmpDeptsDao;
import com.sunten.hrms.ac.dto.AcClockRecordDTO;
import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcClockRecord;
import com.sunten.hrms.ac.dto.AcClockRecordQueryCriteria;
import com.sunten.hrms.ac.service.AcClockRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 打卡记录表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@RestController
@Api(tags = "打卡记录表")
@RequestMapping("/api/ac/clockRecord")
public class AcClockRecordController {
    private static final String ENTITY_NAME = "clockRecord";
    private final AcClockRecordService acClockRecordService;
    private final FndDataScope fndDataScope;
    private final FndUserService fndUserService;
    private final PmEmployeeService pmEmployeeService;
    private final JwtPermissionService jwtPermissionService;
    private final FndDeptService fndDeptService;
    private final AcEmpDeptsDao acEmpDeptsDao;

    @Value("${role.authDocumenter}")
    private String authDocumenter;
    @Value("${role.authRegime}")
    private String authRegime;
    @Value("${role.authTeam}")
    private String authTeam;

    public AcClockRecordController(AcClockRecordService acClockRecordService, FndDataScope fndDataScope, FndUserService fndUserService, PmEmployeeService pmEmployeeService,
                                   JwtPermissionService jwtPermissionService, FndDeptService fndDeptService,AcEmpDeptsDao acEmpDeptsDao) {
        this.acClockRecordService = acClockRecordService;
        this.fndDataScope = fndDataScope;
        this.fndUserService = fndUserService;
        this.pmEmployeeService = pmEmployeeService;
        this.jwtPermissionService = jwtPermissionService;
        this.fndDeptService = fndDeptService;
        this.acEmpDeptsDao = acEmpDeptsDao;
    }

    @Log("新增打卡记录表")
    @ApiOperation("新增打卡记录表")
    @PostMapping
    @PreAuthorize("@el.check('clockRecord:add')")
    public ResponseEntity create(@Validated @RequestBody AcClockRecord clockRecord) {
        return new ResponseEntity<>(acClockRecordService.insert(clockRecord), HttpStatus.CREATED);
    }

    @Log("删除打卡记录表")
    @ApiOperation("删除打卡记录表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('clockRecord:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acClockRecordService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该打卡记录表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改打卡记录表")
    @ApiOperation("修改打卡记录表")
    @PutMapping
    @PreAuthorize("@el.check('clockRecord:edit')")
    public ResponseEntity update(@Validated(AcClockRecord.Update.class) @RequestBody AcClockRecord clockRecord) {
        acClockRecordService.update(clockRecord);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个打卡记录表")
    @ApiOperation("获取单个打卡记录表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('clockRecord:list')")
    public ResponseEntity getClockRecord(@PathVariable Long id) {
        return new ResponseEntity<>(acClockRecordService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询打卡记录表（分页）")
    @ApiOperation("查询打卡记录表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('clockRecord:list')")
    public ResponseEntity getClockRecordPage(AcClockRecordQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
//        if (null != criteria.getDeptId()) {
//            Set<Long> deptIds = new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId()));
//            criteria.setDeptIds(deptIds);
//            criteria.setDeptId(null);
//            // 检测
//            return new ResponseEntity<>(acClockRecordService.listAll(criteria, pageable), HttpStatus.OK);
//        } else {
            return getDataByAuth(criteria, pageable, true);
//        }
    }

    @ErrorLog("查询个人打卡记录表（分页）")
    @ApiOperation("查询个人打卡记录表（分页）")
    @GetMapping(value = "/self")
    @PreAuthorize("@el.check('clockRecord:list')")
    public ResponseEntity getSelfClockRecordPage(AcClockRecordQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {

        return setCriteriaForSelfClock(criteria, pageable, fndUserService, acClockRecordService);
    }

    @ErrorLog("查询打卡记录表（不分页）")
    @ApiOperation("查询打卡记录表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('clockRecord:list')")
    public ResponseEntity getClockRecordNoPaging(AcClockRecordQueryCriteria criteria) {
//        if (null != criteria.getDeptId()) {
//            Set<Long> deptIds = new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId()));
//            criteria.setDeptId(null);
//            criteria.setDeptIds(deptIds);
//            return new ResponseEntity<>(acClockRecordService.listAll(criteria), HttpStatus.OK);
//        } else {
            return getDataByAuth(criteria, null, false);
//        }
    }

    @ErrorLog("导出临时工上月打卡数据")
    @ApiOperation("导出临时工上月打卡数据")
    @GetMapping(value="exportTempEmployeeRecordList")
    @PreAuthorize("@el.check('regime')")
    public void download(HttpServletResponse response) throws IOException {
        acClockRecordService.exportTempEmployeeRecordList(response);
    }

    @ErrorLog("导出打卡记录表数据")
    @ApiOperation("导出打卡记录表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('clockRecord:list')")
    public void download(HttpServletResponse response, AcClockRecordQueryCriteria criteria) throws IOException {
        // 导出要符合权限
//        if (null != criteria.getDeptId()) {
//            Set<Long> deptIds = new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId()));
//            criteria.setDeptId(null);
//            criteria.setDeptIds(deptIds);
//            acClockRecordService.download(acClockRecordService.listAll(criteria), response);
//        } else {
            acClockRecordService.download((List<AcClockRecordDTO>)getDataByAuth(criteria, null, false).getBody(), response);
//            FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
//            Boolean regimeFlag = false;
//            Set<Long> roleDeptIds = new HashSet<>();
//            Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
//            for (GrantedAuthority auth : grantedAuthorityCollection
//            ) {
//                if (auth.getAuthority().equals(authRegime)) {
//                    regimeFlag = true;
//                }
//            }
//            if (regimeFlag) { // 管理员返回全部
//                acClockRecordService.download(acClockRecordService.listAll(criteria), response);
//            }
//            // 检测员工
//            if (null == user.getEmployee()){
//                throw new InfoCheckWarningMessException("检测到该用户未绑定员工，请联系管理员进行员工绑定");
//            }
//            roleDeptIds = acEmpDeptsDao.getDeptsByRolePermission(user.getEmployee().getId(), authDocumenter);
//            for (Long id : roleDeptIds
//            ) {
//                if (id == 1L) { // 检测所有
//                    acClockRecordService.download(acClockRecordService.listAll(criteria), response);
//                }
//            }
//            if (roleDeptIds.size() > 0) { // 有则用roleDeptIds
//                criteria.setDeptIds(roleDeptIds);
//                acClockRecordService.download(acClockRecordService.listAll(criteria), response);
//            } else { // 无 用人事
//                FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptId(), null, false, false, criteria.getDeptIds(), null);
//                if (fndDataScope.isNoDataPermission(dataScopeVo)) { // 无权限
//                    acClockRecordService.download(new ArrayList<>(), response);
//                } else {
//                    if (null != dataScopeVo.getEmployeeId()) {
//                        criteria.setEmployeeId(user.getEmployee().getId());
//                        acClockRecordService.download(acClockRecordService.listAll(criteria), response);
//                    } else {
//                        criteria.setDeptIds(dataScopeVo.getDeptIds());
//                        acClockRecordService.download(acClockRecordService.listAll(criteria), response);
//                    }
//                }
//            }
//        }
//        acClockRecordService.download(acClockRecordService.listAll(criteria), response);
    }

    private ResponseEntity getDataByAuth(AcClockRecordQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, Boolean pageableFlag) {
        System.out.println("look this: " + criteria);
        //        if (null != criteria.getDeptId()) {
//            Set<Long> deptIds = new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId()));
//            criteria.setDeptId(null);
//            criteria.setDeptIds(deptIds);
//        }
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        // 设置是否假打卡记录标记
        criteria.setFakeFlag(acClockRecordService.getFakeRecordSetting(user.getUsername()));

        // 获取权限集合
        Boolean regimeFlag = false;
        Set<Long> roleDeptIds = new HashSet<>();
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority auth : grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals(authRegime)) {
                regimeFlag = true;
            }
        }
        if (regimeFlag) { // 管理员返回全部
            if (pageableFlag) {
                return new ResponseEntity<>(acClockRecordService.listAll(criteria, pageable), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(acClockRecordService.listAll(criteria), HttpStatus.OK);
            }
        }
        // 检测员工
        if (null == user.getEmployee()){
            throw new InfoCheckWarningMessException("检测到该用户未绑定员工，请联系管理员进行员工绑定");
        }
        // 获取资料员权限定义
        roleDeptIds = acEmpDeptsDao.getDeptsByRolePermission(user.getEmployee().getId(), authDocumenter);

        // 获取班组长权限定义
        Set<Long> teamDeptIds = acEmpDeptsDao.getDeptsByRolePermission(user.getEmployee().getId(), authTeam);
        if (teamDeptIds.size() > 0) {
            roleDeptIds.addAll(teamDeptIds);
        }

        for (Long id : roleDeptIds
        ) {
            if (id == 1L) { // 检测所有
                if (pageableFlag) {
                    return new ResponseEntity<>(acClockRecordService.listAll(criteria, pageable), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(acClockRecordService.listAll(criteria), HttpStatus.OK);
                }
            }
        }
        if (roleDeptIds.size() > 0) { // 有则用roleDeptIds
            criteria.setDeptIds(roleDeptIds);
            if (pageableFlag) {
                return new ResponseEntity<>(acClockRecordService.listAll(criteria, pageable), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(acClockRecordService.listAll(criteria), HttpStatus.OK);
            }
        } else { // 无 用人事
            FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptId(), null, false, false, criteria.getDeptIds(), null);
            if (fndDataScope.isNoDataPermission(dataScopeVo)) { // 无权限
                if (pageableFlag) {
                    return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            } else {
                if (null != dataScopeVo.getEmployeeId()) {
                    criteria.setEmployeeId(user.getEmployee().getId());
                } else {
                    criteria.setDeptIds(dataScopeVo.getDeptIds());
                }
                if (pageableFlag) {
                    return new ResponseEntity<>(acClockRecordService.listAll(criteria, pageable), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(acClockRecordService.listAll(criteria), HttpStatus.OK);
                }
            }
        }
    }

    public static ResponseEntity setCriteriaForSelfClock(AcClockRecordQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, FndUserService fndUserService, AcClockRecordService acClockRecordService) {
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        System.out.println("AcClockRecordQueryCriteria: " + criteria);
        criteria.setEmployeeId(fndUserDTO.getEmployee().getId());

        return new ResponseEntity<>(acClockRecordService.listAll(criteria, pageable), HttpStatus.OK);
    }
}
