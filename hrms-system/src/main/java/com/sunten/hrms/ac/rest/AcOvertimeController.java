package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.dao.AcEmpDeptsDao;
import com.sunten.hrms.ac.domain.AcOvertimeApplication;
import com.sunten.hrms.ac.dto.AcOvertimeQueryCriteria;
import com.sunten.hrms.ac.service.AcOvertimeService;
import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 加班工时
 * @atuthor xukai
 * @date 2020/10/19 8:52
 */
@RestController
@RequestMapping("/api/ac/overtime")
public class AcOvertimeController {
    private static final String ENTITY_NAME = "overtimeController";
    private final AcOvertimeService acOvertimeService;
    private final FndUserService fndUserService;
    private final PmEmployeeService pmEmployeeService;
    private final JwtPermissionService jwtPermissionService;
    private final FndDeptService fndDeptService;
    private final AcEmpDeptsDao acEmpDeptsDao;
    private final FndDataScope fndDataScope;

    @Value("${role.authDocumenter}")
    private String authDocumenter;
    @Value("${role.authRegime}")
    private String authRegime;
    @Value("${role.authTeam}")
    private String authTeam;

    public AcOvertimeController(AcOvertimeService acOvertimeService, FndUserService fndUserService, PmEmployeeService pmEmployeeService,
                                JwtPermissionService jwtPermissionService, FndDeptService fndDeptService,FndDataScope fndDataScope,
                                AcEmpDeptsDao acEmpDeptsDao){

        this.acOvertimeService = acOvertimeService;
        this.fndUserService = fndUserService;
        this.pmEmployeeService = pmEmployeeService;
        this.jwtPermissionService = jwtPermissionService;
        this.fndDeptService = fndDeptService;
        this.acEmpDeptsDao = acEmpDeptsDao;
        this.fndDataScope = fndDataScope;
    }

    @ErrorLog("员工加班工时查询（分页）")
    @ApiOperation("员工加班工时查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('overtime:list')")
    public ResponseEntity getOvertimePage(AcOvertimeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        Boolean regimeFlag = false; // 是否管理员
        for (GrantedAuthority auth: grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals(authRegime)) {
                regimeFlag = true;
            }
        }
        // 根据部门Id查询
        if (null != criteria.getDeptId() && !criteria.getDeptId().equals(0L)) {
            PmEmployeeQueryCriteria criteria11 = new PmEmployeeQueryCriteria();
            criteria11.setEnabledFlag(true);
            criteria11.setLeaveFlag(false);
            Set<Long> queryDeptId = new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId()));
            queryDeptId.add(criteria.getDeptId());
            criteria11.setDeptIds(queryDeptId);
            List<PmEmployeeDTO> pmEmployeeDTOList = pmEmployeeService.listAll(criteria11);
            criteria.setQueryEmployees(pmEmployeeDTOList);
        }

        PmEmployeeQueryCriteria pmEmployeeQueryCriteria = new PmEmployeeQueryCriteria();
        if (regimeFlag) { // 考勤管理员
            return new ResponseEntity<>(acOvertimeService.listAll(criteria,pageable), HttpStatus.OK);
        }
        if (null == user.getEmployee()) {
            throw new InfoCheckWarningMessException("检测到该用户未绑定员工，请联系管理员进行员工绑定");
        }
        // 获取考勤范围
        Set<Long> roleDeptIds = acEmpDeptsDao.getDeptsByRolePermission(user.getEmployee().getId(), authDocumenter);
        Set<Long> teamDeptIds = acEmpDeptsDao.getDeptsByRolePermission(user.getEmployee().getId(), authTeam);
        if (teamDeptIds.size() > 0) {
            roleDeptIds.addAll(teamDeptIds);
        }
        for (Long id: roleDeptIds
        ) {
            if (id == 1L) { // 全公司，直接返回
                return new ResponseEntity<>(acOvertimeService.listAll(criteria,pageable), HttpStatus.OK);
            }
        }
        pmEmployeeQueryCriteria.setEnabledFlag(true);
        pmEmployeeQueryCriteria.setLeaveFlag(false);
        if (roleDeptIds.size() > 0){
            pmEmployeeQueryCriteria.setDeptIds(roleDeptIds);
            List<PmEmployeeDTO> pmEmployeeDTOList = pmEmployeeService.listAll(pmEmployeeQueryCriteria);
            criteria.setEmployees(pmEmployeeDTOList);
            return new ResponseEntity<>(acOvertimeService.listAll(criteria,pageable), HttpStatus.OK);
        } else { // 获取岗位权限
            FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptId(), null, false, false, criteria.getDeptIds(), criteria.getEmpId());
            if (fndDataScope.isNoDataPermission(dataScopeVo)){
                return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
            } else {
                if (null != dataScopeVo.getEmployeeId()) {
                    criteria.setWorkCode(pmEmployeeService.getByKey(dataScopeVo.getEmployeeId()).getWorkCard());
                    return new ResponseEntity<>(acOvertimeService.listAll(criteria,pageable), HttpStatus.OK);
                } else {
                    pmEmployeeQueryCriteria.setDeptIds(dataScopeVo.getDeptIds());
                    List<PmEmployeeDTO> pmEmployeeDTOList = pmEmployeeService.listAll(pmEmployeeQueryCriteria);
                    criteria.setEmployees(pmEmployeeDTOList);
                    return new ResponseEntity<>(acOvertimeService.listAll(criteria,pageable), HttpStatus.OK);
                }

//                if (dataScopeVo.getDeptIds().size() > 0) {
//                    pmEmployeeQueryCriteria.setDeptIds(dataScopeVo.getDeptIds());
//                    List<PmEmployeeDTO> pmEmployeeDTOList = pmEmployeeService.listAll(pmEmployeeQueryCriteria);
//                    criteria.setEmployees(pmEmployeeDTOList);
//                    return new ResponseEntity<>(acOvertimeService.listAll(criteria,pageable), HttpStatus.OK);
//                } else {
//                    if (dataScopeVo.getEmployeeId() != null ){
//                        criteria.setWorkCode(pmEmployeeService.getByKey(dataScopeVo.getEmployeeId()).getWorkCard());
//                        return new ResponseEntity<>(acOvertimeService.listAll(criteria,pageable), HttpStatus.OK);
//                    } else {
//                        return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
//                    }
//                }
            }
        }
    }

    @ErrorLog("员工个人加班工时查询（分页）")
    @ApiOperation("员工个人加班工时查询（分页）")
    @GetMapping(value="/self")
    @AnonymousAccess
    public ResponseEntity getSelfOvertimePage(AcOvertimeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        criteria.setWorkCode(fndUserDTO.getEmployee().getWorkCard());

        return new ResponseEntity<>(acOvertimeService.listAll(criteria,pageable), HttpStatus.OK);
    }

    @ErrorLog("从OA端进入，OA审批时修改加班主表")
    @ApiOperation("从OA端进入，OA审批时修改加班主表")
    @PostMapping(value = "/updateFromOA")
    public ResponseEntity updateFromOA(@RequestBody AcOvertimeApplication acOvertimeApplication) {
        acOvertimeService.writeOaApprovalResult(acOvertimeApplication);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
